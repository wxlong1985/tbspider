package com.taobao.spider;

/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultClientIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.protocol.BufferingHttpClientHandler;
import org.apache.http.nio.protocol.EventListener;
import org.apache.http.nio.protocol.HttpRequestExecutionHandler;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.SessionRequest;
import org.apache.http.nio.reactor.SessionRequestCallback;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * Elemental example for executing HTTP requests using the non-blocking I/O
 * model.
 * <p>
 * Please note the purpose of this application is demonstrate the usage of
 * HttpCore APIs. It is NOT intended to demonstrate the most efficient way of
 * building an HTTP client.
 * 
 * 
 */
public class NHttpClient {

	private HttpParams params;

	private ConnectingIOReactor ioReactor;

	private Spiderparam spiderparam;

	public static int MAXCONN = 5;

	private Log log = LogFactory.getLog(NHttpClient.class);

	private BlockingQueue<String> job = new LinkedBlockingQueue<String>(MAXCONN);

	public NHttpClient(Spiderparam spiderparam) throws IOReactorException {
		this.spiderparam = spiderparam;
		params = new SyncBasicHttpParams();
		params
				.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
				.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000)
				.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
						8 * 1024).setBooleanParameter(
						CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
				.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
				.setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla 5.0/1.1");

		ioReactor = new DefaultConnectingIOReactor(2, params);

		HttpProcessor httpproc = new ImmutableHttpProcessor(
				new HttpRequestInterceptor[] { new RequestContent(),
						new RequestTargetHost(), new RequestConnControl(),
						new RequestUserAgent(), new RequestExpectContinue() });

		// We are going to use this object to synchronize between the
		// I/O event and main threads

		BufferingHttpClientHandler handler = new BufferingHttpClientHandler(
				httpproc, new MyHttpRequestExecutionHandler(this.job,this.spiderparam),
				new DefaultConnectionReuseStrategy(), params);

		handler.setEventListener(new EventLogger());

		final IOEventDispatch ioEventDispatch = new DefaultClientIOEventDispatch(
				handler, params);

		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					ioReactor.execute(ioEventDispatch);
				} catch (InterruptedIOException ex) {
					log.error("Interrupted");
				} catch (IOException e) {
					e.printStackTrace();
					log.error("I/O error: " + e.getMessage());
				}
				log.info("Shutdown");
			}

		});
		t.start();
	}

	public void clearJob() {
		this.job.clear();
	}

	public void destroy(int second) throws IOException {
		this.ioReactor.shutdown();
	}

	public void connect() throws InterruptedException {
		while (!this.spiderparam.getQueue().isEmpty()) {
			while(this.job.size() < MAXCONN){
				this.job.put(this.spiderparam.getQueue().take());
				if(this.spiderparam.getQueue().isEmpty())
					break;
			}	
			for (Iterator<String> itor = this.job.iterator(); itor.hasNext();) {
				String url = itor.next();
				log.info("连接信息:"+this.spiderparam.getHttphost().getHostName() + url);
				ioReactor.connect(new InetSocketAddress(this.spiderparam.getHttphost().getHostName(), this.spiderparam.getHttphost().getPort()),null, url,
						new MySessionRequestCallback(this.job,this.spiderparam));
			}
		}

	}

	static class MyHttpRequestExecutionHandler implements
			HttpRequestExecutionHandler {

		private final static String REQUEST_SENT = "request-sent";
		private final static String RESPONSE_RECEIVED = "response-received";
		
		private final static Log log = LogFactory.getLog(MyHttpRequestExecutionHandler.class);

		//private final CountDownLatch requestCount;
		private BlockingQueue<String> job;
		private Spiderparam spiderparam;
		private Object attachment;
		public MyHttpRequestExecutionHandler(BlockingQueue<String> job,Spiderparam spiderparam) {
			super();
			this.job = job;
			this.spiderparam = spiderparam;
			//this.requestCount = requestCount;
		}

		public void initalizeContext(final HttpContext context,
				final Object attachment) {
			this.attachment = attachment;
			context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, this.spiderparam.getHttphost());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.apache.http.nio.protocol.HttpRequestExecutionHandler#finalizeContext(org.apache.http.protocol.HttpContext)
		 *      当connection完成时发生
		 */
		public void finalizeContext(final HttpContext context) {
			log.info("finalizeContext method" + context.getAttribute(ExecutionContext.HTTP_TARGET_HOST));
			
			Object flag = context.getAttribute(RESPONSE_RECEIVED);
			if (flag == null) {
				// Signal completion of the request execution
				log.info("finalizeContext and flag is null");
				//requestCount.countDown();
				this.job.remove(this.attachment);
			}
		}

		public HttpRequest submitRequest(final HttpContext context) {
			HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			Object flag = context.getAttribute(REQUEST_SENT);
			if (flag == null) {
				// Stick some object into the context
				context.setAttribute(REQUEST_SENT, Boolean.TRUE);
				log.info("Sending request to " + targetHost);				
				return new BasicHttpRequest("GET", attachment.toString());
			} else {
				// No new request to submit
				return null;
			}
		}

		public void handleResponse(final HttpResponse response,
				final HttpContext context) {
			HttpEntity entity = response.getEntity();
			try {						
				TagNode node = Spider.cleaner.clean(entity.getContent());
				log.info(this.spiderparam.getHttphost().getHostName() +" "+response.getStatusLine());
				Document doc = Spider.jdomSerializer.createJDom(node);
				log.info(doc.getRootElement().toString());
				List<Element> list = XPath.selectNodes(doc, this.spiderparam.getQueueXpath());
				this.spiderparam.getResults().addAll(list);				
			} catch (IOException ex) {
				log.error("I/O error: " + ex.getMessage());
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			context.setAttribute(RESPONSE_RECEIVED, Boolean.TRUE);
			// System.out.println("正常完成"+requestCount.getCount());
			// Signal completion of the request execution
			//requestCount.countDown();
			this.job.remove(this.attachment);
		}

	}

	/**
	 * @author kingsley 不操作completed部份,捕抓取消 错误 超时事件
	 */
	static class MySessionRequestCallback implements SessionRequestCallback {

		private Log log = LogFactory.getLog(MySessionRequestCallback.class);
		private BlockingQueue<String> job;
		private Spiderparam spiderparam;
		/*public MySessionRequestCallback(final CountDownLatch requestCount) {
			super();
			//this.requestCount = requestCount;
		}*/
		public MySessionRequestCallback(BlockingQueue<String> job,Spiderparam spiderparam) {
			super();
			this.job = job;
			this.spiderparam = spiderparam;
			//this.requestCount = requestCount;
		}

		public void cancelled(final SessionRequest request) {
			log.info("Connect request cancelled: "
					+ request.getRemoteAddress());
			//this.requestCount.countDown();
			Object attachment = request.getAttachment();
			this.job.remove(attachment);
		}

		public void completed(final SessionRequest request) {
		}

		public void failed(final SessionRequest request) {
			log.info("Connect request failed: "
					+ request.getRemoteAddress());
			//this.requestCount.countDown();
			Object attachment = request.getAttachment();
			this.job.remove(attachment);
		}

		/* (non-Javadoc)
		 * @see org.apache.http.nio.reactor.SessionRequestCallback#timeout(org.apache.http.nio.reactor.SessionRequest)
		 * 超时的时间处理将不移除任务对象,将要解析的URL发回重审
		 */
		public void timeout(final SessionRequest request) {
			log.info("Connect request timed out: "
					+ request.getRemoteAddress());
			//this.requestCount.countDown();
			Object attachment = request.getAttachment();
			this.job.remove(attachment);
			try {
				this.spiderparam.getQueue().put(attachment.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				log.error(e);
			}
			
		}

	}

	/**
	 * @author kingsley 事件处理
	 */
	static class EventLogger implements EventListener {
		private Log log = LogFactory.getLog(EventLogger.class);

		public void connectionOpen(final NHttpConnection conn) {
			log.info("Event: Connection open: " + conn);
		}

		public void connectionTimeout(final NHttpConnection conn) {
			log.error("Event: Connection timed out: "
					+ conn.getContext().getAttribute(
							ExecutionContext.HTTP_TARGET_HOST));

		}

		public void connectionClosed(final NHttpConnection conn) {
			log.info("Event: Connection closed: " + conn);
		}

		public void fatalIOException(final IOException ex,
				final NHttpConnection conn) {
			log.error("I/O error: " + ex.getMessage());
		}

		public void fatalProtocolException(final HttpException ex,
				final NHttpConnection conn) {
			log.error("HTTP error: " + ex.getMessage());
		}

	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOReactorException, InterruptedException {
		Spiderparam spiderparam = Spiderparam.productList("靴子");
		Spider spider = new Spider();
		spider.setSpiderparam(spiderparam);		
		spider.connect();
		
	}

}

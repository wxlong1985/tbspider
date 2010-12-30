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
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

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

/**
 * Elemental example for executing HTTP requests using the non-blocking I/O model.
 * <p>
 * Please note the purpose of this application is demonstrate the usage of HttpCore APIs.
 * It is NOT intended to demonstrate the most efficient way of building an HTTP client. 
 *
 * 
 */
public class NHttpClient {
	
	private HttpParams params;
	
	private ConnectingIOReactor ioReactor;
	
	private Spiderparam spiderparam;
	
	public NHttpClient(Spiderparam spiderparam) throws IOReactorException{
		this.spiderparam = spiderparam;
		params = new SyncBasicHttpParams();
        params
            .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
            .setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000)
            .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
            .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
            .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
            .setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla 5.0/1.1");
        	
        ioReactor = new DefaultConnectingIOReactor(2, params);

        HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
                new RequestContent(),
                new RequestTargetHost(),
                new RequestConnControl(),
                new RequestUserAgent(),
                new RequestExpectContinue()});
        
        // We are going to use this object to synchronize between the 
        // I/O event and main threads
        
        BufferingHttpClientHandler handler = new BufferingHttpClientHandler(
                httpproc,
                new MyHttpRequestExecutionHandler(this.spiderparam),
                new DefaultConnectionReuseStrategy(),
                params);

        handler.setEventListener(new EventLogger());
        
        final IOEventDispatch ioEventDispatch = new DefaultClientIOEventDispatch(handler, params);
        
        Thread t = new Thread(new Runnable() {
         
            public void run() {
                try {
                    ioReactor.execute(ioEventDispatch);
                } catch (InterruptedIOException ex) {
                    System.err.println("Interrupted");
                } catch (IOException e) {
                    System.err.println("I/O error: " + e.getMessage());
                }
                System.out.println("Shutdown");
            }
            
        });
        t.start();
	}
	

    public static void main(String[] args) throws Exception {
        

        SessionRequest req = ioReactor.connect(
                new InetSocketAddress("item.taobao.com", 80), 
                null, 
                new HttpHost("item.taobao.com"),
                new MySessionRequestCallback(requestCount));
        /*reqs[1] = ioReactor.connect(
                new InetSocketAddress("www.google.com", 80), 
                null,
                new HttpHost("www.google.ch"),
                new MySessionRequestCallback(requestCount));
        reqs[2] = ioReactor.connect(
                new InetSocketAddress("www.apache.org", 80), 
                null,
                new HttpHost("www.apache.org"),
                new MySessionRequestCallback(requestCount));*/
     
        // Block until all connections signal
        // completion of the request execution
        
        requestCount.await();
        
        System.out.println("Shutting down I/O reactor");
        
        ioReactor.shutdown();
        
        System.out.println("Done");
    }
    
    static class MyHttpRequestExecutionHandler implements HttpRequestExecutionHandler {

        private final static String REQUEST_SENT       = "request-sent";
        private final static String RESPONSE_RECEIVED  = "response-received";
        
        private final CountDownLatch requestCount;
        
        public MyHttpRequestExecutionHandler(final CountDownLatch requestCount) {
            super();
            this.requestCount = requestCount;
        }
        
        public void initalizeContext(final HttpContext context, final Object attachment) {
            HttpHost targetHost = (HttpHost) attachment;
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, targetHost);
        }
        
        /* (non-Javadoc)
         * @see org.apache.http.nio.protocol.HttpRequestExecutionHandler#finalizeContext(org.apache.http.protocol.HttpContext)
         * 当connection完成时发生
         */
        public void finalizeContext(final HttpContext context) {
        	System.out.println("finalizeContext" + context.getAttribute(ExecutionContext.HTTP_TARGET_HOST));
            Object flag = context.getAttribute(RESPONSE_RECEIVED);
            if (flag == null) {
                // Signal completion of the request execution
            	System.out.println("finalizeContext and flag is null");
                requestCount.countDown();
            }
        }

        public HttpRequest submitRequest(final HttpContext context) {
            HttpHost targetHost = (HttpHost) context.getAttribute(
                    ExecutionContext.HTTP_TARGET_HOST);
            Object flag = context.getAttribute(REQUEST_SENT);
            if (flag == null) {
                // Stick some object into the context
                context.setAttribute(REQUEST_SENT, Boolean.TRUE);

                System.out.println("--------------");
                System.out.println("Sending request to " + targetHost);
                System.out.println("--------------");
               // BasicHttpRequest request = new BasicHttpRequest("GET", "/item.htm?id=7465983371");
                //request.               
                return new BasicHttpRequest("GET", "/item.htm?id=7465983371");
            } else {
                // No new request to submit
                return null;
            }
        }
        
        public void handleResponse(final HttpResponse response, final HttpContext context) {
            HttpEntity entity = response.getEntity();
            try {
                String content = EntityUtils.toString(entity);
                
                System.out.println("--------------"+context.getAttribute(ExecutionContext.HTTP_TARGET_HOST));
                System.out.println(response.getStatusLine());
                System.out.println("--------------");
                System.out.println("Document length: " + content.length());
                System.out.println("--------------");
            } catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            }

            context.setAttribute(RESPONSE_RECEIVED, Boolean.TRUE);
            //System.out.println("正常完成"+requestCount.getCount());
            // Signal completion of the request execution
            requestCount.countDown();
        }
        
    }
    
    /**
     * @author kingsley
     * 不操作completed部份,捕抓取消 错误 超时事件
     */
    static class MySessionRequestCallback implements SessionRequestCallback {

        private final CountDownLatch requestCount;        
        
        public MySessionRequestCallback(final CountDownLatch requestCount) {
            super();
            this.requestCount = requestCount;
        }
        
        public void cancelled(final SessionRequest request) {
            System.out.println("Connect request cancelled: " + request.getRemoteAddress());
            this.requestCount.countDown();
        }

        public void completed(final SessionRequest request) {
        }

        public void failed(final SessionRequest request) {
            System.out.println("Connect request failed: " + request.getRemoteAddress());
            this.requestCount.countDown();
        }

        public void timeout(final SessionRequest request) {
            System.out.println("Connect request timed out: " + request.getRemoteAddress());
            this.requestCount.countDown();
        }
        
    }
    
    /**
     * @author kingsley
     * 事件处理
     */
    static class EventLogger implements EventListener {

        public void connectionOpen(final NHttpConnection conn) {
            System.out.println("Event: Connection open: " + conn);
        }

        public void connectionTimeout(final NHttpConnection conn) {
            System.out.println("Event: Connection timed out: " + conn.getContext().getAttribute(ExecutionContext.HTTP_TARGET_HOST));
            
        }

        public void connectionClosed(final NHttpConnection conn) {
            System.out.println("Event: Connection closed: " + conn);
        }

        public void fatalIOException(final IOException ex, final NHttpConnection conn) {
            System.err.println("I/O error: " + ex.getMessage());
        }

        public void fatalProtocolException(final HttpException ex, final NHttpConnection conn) {
            System.err.println("HTTP error: " + ex.getMessage());
        }
        
    }

}

package com.example.anubis9000.saleandorid.SSL;

/**
 * Created by anubis9000 on 2017/12/29.
 */
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
// 注意这里的SSLSocketFactory是org.apache.http.conn.ssl包中的
import org.apache.http.conn.ssl.SSLSocketFactory;

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    public MySSLSocketFactory(KeyStore truststore) throws
            NoSuchAlgorithmException,KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        sslContext.init(null, null, new SecureRandom());
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}

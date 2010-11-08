package extensions;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.regex.Pattern;

import dareka.common.Logger;
import dareka.extensions.*;
import dareka.processor.*;
import dareka.processor.impl.Cache;

/**
 * 串設定を http://localhost:8080/proxy.pac で読み込めるようにする拡張
 */
public class nlProxyPac implements Extension2, Processor {
    //version
    @Override
    public String getVersionString() {
        return "nlProxyPac ver1.0";
    }

    // Extension2 interface
    @Override
    public void registerExtensions(ExtensionManager extensionmanager) {
        extensionmanager.registerProcessor(this);
    }

    // 処理するHTTPのメソッド
    @Override
    public String[] getSupportedMethods() {
        return new String[] { "GET" };
    }

    // 処理するURLの正規表現
    @Override
    public Pattern getSupportedURLAsPattern() {
        return Pattern.compile("^/proxy\\.pac$");
    }

    @Override
    public String getSupportedURLAsString() {
        return null;
    }

    // 該当するリクエストの際に呼ばれる
    @Override
    public Resource onRequest(HttpRequestHeader requestHeader, Socket browser)
        throws IOException {

        FileChannel ch = new FileInputStream("./proxy.pac").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int)ch.size());

        ch.read(buffer);
        buffer.clear();
        byte pacData[] = new byte[buffer.capacity()];
        buffer.get(pacData);

        ch.close();

        StringResource r = new StringResource(pacData);
        r.setResponseHeader("Content-Type", "text/plain");
        Logger.info("nlProxyPact ver1.0: return proxy.pac");

        return r;
    }
}

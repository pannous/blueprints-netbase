package com.pannous.util;

import com.pannous.netbase.blueprints.Debugger;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class Internet {

    public class PasswordAuthenticationFailedException extends Exception {}

    private static int default_timeout = 3000;// unless specified
    public static String encoding = "UTF-8";
    private static String authName;
    private static String authPass;

    public static String download(String address, String localFileName,int timeout) throws Exception {
        Debugger.info("downloading " + address);
        address = address.replaceAll(" ", "%20");
        String txt = new String(new byte[]{}, encoding);
        OutputStream out = null;
        if (localFileName != null)
            try {
                out = new BufferedOutputStream(new FileOutputStream(
                        localFileName));
            } catch (Exception e1) {
                Debugger.info(e1);
            }
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            StringBuffer out2 = new StringBuffer();
            conn = url.openConnection(Proxy.NO_PROXY);
            conn.setDoOutput(true);
//			conn.setUseCaches(true);
//          conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//          conn.setRequestProperty("Accept", "application/xhtml+xml,application/xml");// text/html,
//			if(authName!=null)
//			conn.setRequestProperty("Authorization","Basic			"+authName+":"+authPass);
            conn.setReadTimeout(timeout);// 500 msecs
            conn.setConnectTimeout(timeout);
//			 }
            try {
                in = conn.getInputStream();
            } catch (Exception e1) {// FileNotFoundException krasser android 4 bug ??
                in = url.openStream();
            }
            if ("gzip".equals(conn.getContentEncoding()))
                try {
                    in = new GZIPInputStream(in);
                } catch (Exception e) {
                }
            // Reader r = new InputStreamReader(in, encoding);
            // Writer w = new OutputStreamWriter(out, encoding);
            // int data = r.read();
            // while (data != -1) {
            // w.append((char) data);
            // data = r.read();
            // }
            //
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                if (out != null)
                    out.write(buffer, 0, numRead);
                else {
                    out2.append(new String(buffer, 0, numRead, encoding));
                }
                numWritten += numRead;
            }
            txt = out2.toString();

        } catch (java.io.IOException e) {
            Debugger.error(e);
        } catch (Exception e) {
            Debugger.info(e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception ioe) {
//				throw ioe;
            }
        }
        authName = null;
        authPass = null;// reset!
        return txt;
    }

    public static String postRequest(String url0, String param, Map<String, String> fields, int timeout) {
        OutputStreamWriter wr = null;
        BufferedReader rd = null;
        String s="";
        try {
            // Send data
            URL url = new URL(url0);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            if (url0.contains("mashape.com"))
                conn.setRequestProperty("X-Mashape-Authorization", "r1pow8oalexjfypdklgfz2kxvjhjmt");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Voice Actions http://pannous.info)");
//			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US;.1.6) Gecko/20070725 Firefox/2.0.0.6");

            wr = new OutputStreamWriter(conn.getOutputStream());

            if (fields != null && !fields.isEmpty()) {
                String CrLf = "\r\n";
                String message1 = "";
                String boundary = "------------------------1c9f3f6cf45b42ec";
                message1 += CrLf;
                for (String field : fields.keySet()) {
                    message1 += "--" + boundary + CrLf;
                    message1 += "Content-Disposition: form-data; name=\"" + field + "\"" + CrLf;
                    message1 += CrLf;
                    message1 += fields.get(field) + CrLf;
                }
                message1 += "--" + boundary + "--" + CrLf;
                param += message1;
                int length = param.length();
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setRequestProperty("Content-Length", String.valueOf(length));
            }
            wr.write(param);
            wr.flush();

            // Get the response
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                s += line;
            }
        } catch (java.io.IOException e) {
            Debugger.info("VoiceActions " + e.toString() + " " + url0);
        } catch (Exception e) {
            Debugger.info("VoiceActions " + e.toString() + " " + url0);

        } finally {
            try {
                if (wr != null)
                    wr.close();
                if (rd != null)
                    rd.close();
            } catch (Exception ioe) {
            }
        }
        return s;
    }

    public static String download(String address) throws Exception {
        return download(address, null, default_timeout);
    }

    public static String downloadSecure(String address, String login,
                                        String password) throws Exception {
        try {
            prepareHttpsRequest(login, password);
            return download(address, null, default_timeout);
        } finally {
            prepareHttpsRequest("reset", "password");
        }
    }

    private static boolean alreadyTried = false;

    public static void prepareHttpsRequest(final String login,
                                           final String password) {
        alreadyTried = false;
        authName = login;
        authPass = password;
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                if (alreadyTried)
                    return null;
                // throw new PasswordAuthenticationFailedException();
                alreadyTried = true;
                return new PasswordAuthentication(login, password.toCharArray());
            }
        });
    }
}

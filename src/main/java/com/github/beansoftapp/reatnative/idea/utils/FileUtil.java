package com.github.beansoftapp.reatnative.idea.utils;

import java.io.*;

/**
 * FileUtil. Simple file operation class.
 * 2009-12-22 修正多线程下可能产生的并发BUG
 *
 * @author BeanSoft
 *
 */
public class FileUtil {


    /**
     * Read content from local file. FIXME How to judge UTF-8 and GBK, the
     * correct code should be: FileReader fr = new FileReader(new
     * InputStreamReader(fileName, "ENCODING")); Might let the user select the
     * encoding would be a better idea. While reading UTF-8 files, the content
     * is bad when saved out.
     *
     * @param fileName -
     *            local file name to read
     * @return
     * @throws Exception
     */
    public static String readFileAsString(String fileName) throws Exception {
        String content = new String(readFileBinary(fileName));

        return content;
    }

    /**
     * 读取文件并返回为给定字符集的字符串.
     * @param fileName
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String readFileAsString(String fileName, String encoding) throws Exception {
        String content = new String(readFileBinary(fileName), encoding);

        return content;
    }

    /**
     * 读取文件并返回为给定字符集的字符串.
     * @param fileName
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String readFileAsString(InputStream in) throws Exception {
        String content = new String(readFileBinary(in));

        return content;
    }

    /**
     * Read content from local file to binary byte array.
     *
     * @param fileName -
     *            local file name to read
     * @return
     * @throws Exception
     */
    public static byte[] readFileBinary(String fileName) throws Exception {
        FileInputStream fin = new FileInputStream(fileName);

        return readFileBinary(fin);
    }

    /**
     * 从输入流读取数据为二进制字节数组.
     * @param streamIn
     * @return
     * @throws IOException
     */
    public static byte[] readFileBinary(InputStream streamIn) throws IOException {
        BufferedInputStream in = new BufferedInputStream(streamIn);

        ByteArrayOutputStream out = new ByteArrayOutputStream(10240);

        /**
         * The buffer.
         */
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) >= 0)
            out.write(buf, 0, len);
        in.close();

        return out.toByteArray();
    }

    /**
     * Write string content to local file.
     *
     * @param fileName -
     *            local file name will write to
     * @param content
     *            String text
     * @return true if success
     * @throws IOException
     */
    public static boolean writeFileString(String fileName, String content)
            throws IOException {
        FileWriter fout = new FileWriter(fileName);
        fout.write(content);
        fout.close();
        return true;
    }

    /**
     * Write string content to local file using given character encoding.
     *
     * @param fileName -
     *            local file name will write to
     * @param content
     *            String text
     * @param encoding
     *            the encoding
     * @return true if success
     * @throws IOException
     */
    public static boolean writeFileString(String fileName, String content,
                                          String encoding) throws IOException {
        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(
                fileName), encoding);

        fout.write(content);
        fout.close();
        return true;
    }

    /**
     * Write binary byte array to local file.
     *
     * @param fileName -
     *            local file name will write to
     * @param content
     *            binary byte array
     * @return true if success
     * @throws IOException
     */
    public static boolean writeFileBinary(String fileName, byte[] content)
            throws IOException {
        FileOutputStream fout = new FileOutputStream(fileName);
        fout.write(content);
        fout.close();
        return true;
    }

    /**
     * 检查文件名是否合法.文件名字不能包含字符\/:*?"<>|
     *
     * @param fileName文件名,不包含路径
     * @return boolean is valid file name
     */
    public static boolean isValidFileName(String fileName) {
        boolean isValid = true;
        String errChar = "\\/:*?\"<>|"; //
        if (fileName == null || fileName.length() == 0) {
            isValid = false;
        } else {
            for (int i = 0; i < errChar.length(); i++) {
                if (fileName.indexOf(errChar.charAt(i)) != -1) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    /**
     * 把非法文件名转换为合法文件名.
     *
     * @param fileName
     * @return
     */
    public static String replaceInvalidFileChars(String fileName) {
        StringBuffer out = new StringBuffer();

        for (int i = 0; i < fileName.length(); i++) {
            char ch = fileName.charAt(i);
            // Replace invlid chars: \\/:*?\"<>|
            switch (ch) {
                case '\\':
                case '/':
                case ':':
                case '*':
                case '?':
                case '\"':
                case '<':
                case '>':
                case '|':
                    out.append('_');
                    break;
                default:
                    out.append(ch);
            }
        }

        return out.toString();
    }

    /**
     * Convert a given file name to a URL(URI) string.
     *
     * @param fileName -
     *            the file to parse
     * @return - URL string
     */
    public static String filePathToURL(String fileName) {
        String fileUrl = new File(fileName).toURI().toString();
        return fileUrl;
    }

    /**
     * Write string content to local file.
     *
     * @param fileName -
     *            local file name will write to
     * @param content
     *            String text
     * @return true if success
     * @throws IOException
     */
    public static boolean appendFileString(String fileName, String content)
            throws IOException {
        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(
                fileName, true), "GBK");

        fout.write(content);
        fout.close();
        return true;
    }

    public static void main(String[] args) {
        System.out.println(replaceInvalidFileChars("http://www.abc.com/"));
    }
}
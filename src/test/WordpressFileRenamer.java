import java.io.File;
import java.io.UnsupportedEncodingException;

public class WordpressFileRenamer {
    public static void main(String[] args) throws Exception {
        String inputDir = "/Users/beansoft/Documents/git/pilipa/beansoftapp.github.io/_posts";
        File rootDir = new File(inputDir);
        String[] posts = rootDir.list();
        if(posts != null) {
            for(String postName : posts) {
                String chnName = decode(postName);
                System.out.println(chnName);
                if(chnName != null && !chnName.equals(postName)) {
                    new File(inputDir, postName).renameTo(new File(inputDir, chnName));
                }

            }
        }
    }

    static String decode(String input) {
        try {
            String out = java.net.URLDecoder.decode(
                    input, "UTF-8");
            return out;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }
}

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentRemover {

  private static final String FILE_PATH = "/Users/beansoft/Documents/git/pilipa/pilipa-shop-product/src/main/java/cn/pilipa/shop/product/entity/SitePage.java";

  public static void main(String[] args) throws Exception {
    ParseResult<CompilationUnit> result = new JavaParser().parse(new File(FILE_PATH));
    CompilationUnit cu = null;
    if (result.isSuccessful()) {
      Optional<CompilationUnit> optionalCompilationUnit = result.getResult();
      if (optionalCompilationUnit.isPresent()) {
        cu = optionalCompilationUnit.get();
      }
    }

    if (cu == null) {
      System.err.println("解析失败, 返回");
      return;
    }

    List<Comment> comments = cu.getAllContainedComments();
    List<Comment> unwantedComments = comments
        .stream()
        .filter(p -> !p.getCommentedNode().isPresent() || p instanceof LineComment || p instanceof JavadocComment)
        .collect(Collectors.toList());

    if (unwantedComments.size() > 0) {
      System.err.println(unwantedComments.get(0).toString());
      if (unwantedComments.get(0).toString().contains("@author")) {
        unwantedComments.remove(0);
      }
    }

    unwantedComments.forEach(Node::remove);

    /*
    List<TypeDeclaration<?>> types = cu.getTypes();
    for (TypeDeclaration type : types) {
      List<BodyDeclaration> members = type.getMembers();
      for (BodyDeclaration member : members) {
        if (member instanceof FieldDeclaration) {
          FieldDeclaration f = (FieldDeclaration) member;
          Optional<Javadoc> doc = f.getJavadoc();
          if (doc.isPresent()) {
            JavadocDescription content = doc.get().getDescription();

            if (content != null) {
              VariableDeclarator var = f.getVariables().get(0);

              String typeName = var.getTypeAsString();
              if(typeName.toLowerCase().contains("enum")) {
                typeName = "String";
              }
              System.out.println( typeName + " " + var.getName() + ";// " + content.toText());
            }

          }

        }
      }
    }

     */

    System.out.println(cu.toString());

  }

}
/* 파일 업로드 : 업로드 파일 저장하기
 * => 저장 위치:
 *    1) 웹 브라우저에서 요청할 수 있는 경로
 *       - 웹 애플리케이션이 배치된 폴더에 저장하는 방식
 *         => 개발 중인 경우 배치폴더: .../tmp0/wtpwebapps/web01/...
 *         => 운영 중인 경우 배치폴더: .../톰캣서버설치폴더/webapps/web01/... 
 *       - 웹 브라우저에서 직접 다운로드를 요청할 수 있어 편하다.
 *       - 단점
 *         직접 경로에 있는 파일을 요청할 수 있기 때문에 다른 파일도 쉽게 다운로드 할 수 있다.
 *         즉 파일에 대한 권한 관리가 힘들다!
 *       - 그래서 실무에서는 업로드 파일을 별도의 디렉토리나 클라우드 서버에 저장한다.
 *       
 *    2) 웹 브라우저에서 요청할 수 없는 경로(실무 방식)
 *       - 웹 애플리케이션이 배치된 폴더가 아닌 디렉토리에 저장하는 방식
 *       - 웹 브라우저에서 파일이 있는 경로를 직접 요청할 수 없다.
 *       - 반드시 다운로드를 해주는 서블릿의 도움을 받아야 한다.
 *       - 장점
 *         다운로드의 권한을 제어할 수 있다.
 *       - 단점
 *         웹 브라우저에서 직접 파일의 저장된 경로를 지정하여 파일을 다운로드 할 수 없다.
 */
package step14;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/step14/Servlet04")
@SuppressWarnings("serial")
public class Servlet04 extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    DiskFileItemFactory itemFactory = new DiskFileItemFactory();
    ServletFileUpload multipartDataParser = new ServletFileUpload(itemFactory);
    
    try {
      List<FileItem> parts = multipartDataParser.parseRequest(req);
      HashMap<String,FileItem> partMap = new HashMap<>();
      for (FileItem part : parts) {
        partMap.put(part.getFieldName(), part);
      }
      
      String name = partMap.get("name").getString("UTF-8"); 
      
      FileItem part = partMap.get("file1");
      String file1 = part.getName();
      ServletContext sc = this.getServletContext();
      // 현재 웹 애플리케이션의 폴더 위치를 기준으로 경로를 계산하고 싶다면,
      // 다음과 같이 ServletContext.getRealPath()를 사용하라!
      File uploadPath = new File(sc.getRealPath("/step14/upload/" + file1));
      
      // 클라이언트가 보낸 파일은 임시 폴더에 보관되어 있다.
      // 이렇게 임시 폴더에 보관된 데이터를 우리가 지정하는 폴더로 옮기려면
      // 다음의 코드를 실행하라!
      part.write(uploadPath);  
                                
      part = partMap.get("file2");
      String file2 = part.getName(); // 오리지널 파일명 꺼내기
      uploadPath = new File(sc.getRealPath("/step14/upload/" + file2)); // 저장할 장소를 준비하고,
      part.write(uploadPath); // 임시 폴더에 저장된 파일을 우리가 지정한 경로로 옮긴다.
      
      
      resp.setContentType("text/plain;charset=UTF-8");
      PrintWriter out = resp.getWriter();
      out.printf("name=%s\n", name);
      out.printf("file1=%s\n", file1);
      out.printf("file2=%s\n", file2);

    } catch (Exception e) {
      throw new ServletException(e);
    }
    
  }
  
}

/* HTTP POST 요청 프로토콜
POST /web01-bit/step14/Servlet02 HTTP/1.1
Host: localhost:8080
Content-Length: 13653
Pragma: no-cache
Cache-Control: no-cache
Origin: http://localhost:8080
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary4lpFkjYXAQS2YzjE
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,...
Referer: http://localhost:8080/web01-bit/step14/form2.html
Accept-Encoding: gzip, deflate, br
Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4

------WebKitFormBoundary4lpFkjYXAQS2YzjE
Content-Disposition: form-data; name="name"

홍길동
------WebKitFormBoundary4lpFkjYXAQS2YzjE
Content-Disposition: form-data; name="file1"; filename="images.jpeg"
Content-Type: image/jpeg

���� JFIF      �� �  ( %!1!%)+...383-7(-....
...
------WebKitFormBoundary4lpFkjYXAQS2YzjE
Content-Disposition: form-data; name="file2"; filename="img01.jpeg"
Content-Type: image/jpeg

���� JFIF      �� �  ( %!1!%)+...383-7(-.+...
..
------WebKitFormBoundary4lpFkjYXAQS2YzjE--

 */











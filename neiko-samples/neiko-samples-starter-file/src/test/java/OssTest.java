
import com.schilings.neiko.autoconfigure.file.core.FileClient;
import com.schilings.neiko.autoconfigure.oss.OssClient;
import com.schilings.neiko.autoconfigure.oss.domain.StreamTemp;
import com.schilings.neiko.samples.file.FileApplication;
import com.schilings.neiko.samples.file.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 
 * <p></p>
 * <img src="https://schilings-ballcat.oss-cn-shenzhen.aliyuncs.com/2.jpg"></img>
 * 
 * @author Schilings
*/
@SpringBootTest(classes = FileApplication.class)
public class OssTest {

    private static final File OPERATE_FILE = new File("E:\\PS files\\pic\\普通图\\1(1).jpg");
    
    
    @Autowired
    private OssClient ossClient;

    
    @Autowired
    private FileClient fileClient;

    @Autowired
    private FileService fileService;


    @Test
    public void testFileService() throws IOException {
        FileInputStream inputStream = new FileInputStream(OPERATE_FILE);
        StreamTemp temp = ossClient.getSize(inputStream);
        fileService.upload(temp.getStream(), "1.jpg", temp.getSize());
        
    }

    @Test
    public void testFileClient() throws IOException {
        FileInputStream inputStream = new FileInputStream(OPERATE_FILE);
        fileClient.upload(inputStream, "2.jpg");
    }

    @Test
    public void testOssClient() throws IOException {
        FileInputStream inputStream = new FileInputStream(OPERATE_FILE);
        final String relativePath = "2.jpg";
        
        ossClient.upload(inputStream, relativePath);
        inputStream.close();
        System.out.println(ossClient.getDownloadUrl(relativePath));

    }
        
}

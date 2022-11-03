import com.hexing.RuoYiApplication;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.service.IAssetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TestAssetService.class)
@RunWith(SpringRunner.class)
public class TestAssetService {
    @Autowired
    private IAssetService assetService;

    @Test
    public void testSelectAssetByUserName() {
        List<String> userCodeList = new ArrayList<>();
        userCodeList.add("80010712");
        List<Asset> assetList = assetService.selectAssetByUserName(userCodeList);
        System.out.println(assetList);
    }

}

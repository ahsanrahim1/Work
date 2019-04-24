import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.wmap.eisl.registrationService.controller.RegistrationServiceController;
import com.ubs.wmap.eisl.registrationService.exception.DataNotFoundException;
import com.ubs.wmap.eisl.registrationService.exception.EsilTokenNotValidException;
import com.ubs.wmap.eisl.registrationService.exception.InvalidDataException;
import com.ubs.wmap.eisl.registrationService.model.ColumnReferenceRequestVO;
import com.ubs.wmap.eisl.registrationService.model.PayloadSO;
import com.ubs.wmap.eisl.registrationService.model.RegistrationSO;
import com.ubs.wmap.eisl.registrationService.model.ResponseSO;
import com.ubs.wmap.eisl.registrationService.model.RoleRequestVO;
import com.ubs.wmap.eisl.registrationService.model.RowReferenceRequestVO;
import com.ubs.wmap.eisl.registrationService.service.RegistrationServiceImpl;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

	@Mock
	RegistrationServiceController regServiceController;
	
	@Mock
	RegistrationServiceImpl registrationService;

        
        @Test
        public void resgistrationPostTest() throws Exception{
            PayloadSO testPayload = new PayloadSO();
            testPayload.setCompany("testCompany");
            testPayload.setUserId("testUserId");
            RoleRequestVO testRole = new RoleRequestVO();
            testRole.setConsume("testConsume");
            testRole.setPublish("setPublish");
            ColumnReferenceRequestVO testColRef = new ColumnReferenceRequestVO();
            testColRef.setName("test column name");
            testColRef.setType("test column type");
            Set<ColumnReferenceRequestVO> testColumnRefSet = new HashSet<>();
            testColumnRefSet.add(testColRef);
            RowReferenceRequestVO testRowRef = new RowReferenceRequestVO();
            testRowRef.setName("test row name");
            testRowRef.setType("test row type");
            Set<RowReferenceRequestVO> testRowRefSet = new HashSet<>();
            testRowRefSet.add(testRowRef);
            String eislToken = "eyJhbGciOiJSUzI1NiJ9.eyJlbnRpdGxlbWVudHMiOnsiUkVBRE9OTFlfUEVSTUlTU0lPTlMiOjMyMzMxMiwiRlVMTF9BQ0NFU1NfUEVSTUlTU0lPTlMiOjk4NzY1NCwiRURJVF9QRVJNSVNTSU9OUyI6MTQ1MzIyLCJBRE1JTl9QRVJNSVNTSU9OUyI6NjM2MzY2fSwicm9sZSI6ImFkbWluIiwiY29ycmVsYXRpb25JZCI6IjU3YzJkYjQyLTk1ZGMtNDRkZi1hZjUxLTRmNWY2OTY4NGIzNyIsInVzZXJOYW1lIjoiVXRrYXJzaCIsInNlcnZpY2VJZCI6ImdldF9hY2MiLCJleHAiOjE1NTc2MzMzODUsImp0aSI6ImQ4OTZlMzdkLTAyYzAtNDNkOC1hNTQ0LTBhODY1ZDc4MTk5YSJ9.PAS3x8devMLILDVAyK4EIpcmCMTAWCtuLnczUjb0zF0r_cTCPHs1cd-P-NaZ936SfdZX6u2qvvI7yDiOTQbhMawV5L8sXgZEeo0a_n6c9H68cvLfrI1Io-8sBUjdzmKAjHcfdEjFm7Ayo5CLGghYSB9Qbq28mVQlAlE_aQD5PJvoHOUqtFFqRaEHmspMUSp_RAyZkE4MelnaBM-_8eLtNI_QdHXtMXIkSm3vCa7sSI4VoEj9IvfCDhnvnO0Qb8acHj86P1zbdVxup9dpe0kETwIwndenamHH5HWXp1h1gvWnHuX45bcYxdS8Ui4vhfD7mnU3V0Hv0cAKEfNw1RLKbzNfcEUrmjMIIPewyOsBru5HPz9K9H3JhlaUGlIW7YOgrwYJTKSk57Ap9grehVdt6zG9NJlP25yJcz_f8JMUV-s5H8xZEiq8LvYWU45RKZ7UQebD8TRMfy45amSe2hN0M_iYvveF4Sy72B_tCX6Mw0lNfTfmAmCsSwWoLJBxkwxhNnTKMtDWc6QbmlaqXae0Udj0lC3osQ4kdPaZyxVTVVwTTqNeXN4CRPMt-GiUt2g7bhMo2dXNttlxbTaG7RqtKT2t1pgNWto5Xd_-GV3XTcql-SGz_Vglyd_7oi_tquODEKSe5-k3nGyLkpg2Uv5ICLS1DIt8U0XYfZdZ4XIg9Vs";
            RegistrationSO testRegistration = RegistrationSO.builder().userId(testPayload.getUserId()).company(testPayload.getCompany()).eislToken(eislToken).dataEntitlement("testDataEntitlement").role(testRole).serviceId("testServiceId").userName("testUserName").columnReferences(testColumnRefSet).rowReferences(testRowRefSet).serviceId("testServiceId").build();
            ResponseEntity<RegistrationSO> responseEntity =new ResponseEntity(testRegistration, HttpStatus.CREATED);   
            Mockito.lenient().when(registrationService.validateToken(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
            Mockito.when(regServiceController.addRegistry("testBasic", eislToken, testPayload)).thenReturn(responseEntity);
            ResponseEntity<RegistrationSO> expectedResponse = regServiceController.addRegistry("basic", eislToken, testPayload);
            assertEquals("Success", responseEntity.getBody(), expectedResponse.getBody());

            
        }
}

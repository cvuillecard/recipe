package revisions.tu;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {

    @Test
    public void simpleMockList() {
        final List<String> mockList = Mockito.mock(List.class);

        mockList.add("toto");
        mockList.add("titi");
        mockList.add("tata");

        Mockito.verify(mockList, Mockito.times(3)).add(Mockito.anyString());
        Mockito.verify(mockList).add("toto");
        Mockito.verify(mockList).add("titi");
        Mockito.verify(mockList).add("tata");

        Assert.assertEquals(0, mockList.size());

        Mockito.doReturn(1).when(mockList).size();
        Assert.assertEquals(1, mockList.size());
    }


    @Test
    public void simpleSpyList() {
        final List<String> mockList = Mockito.spy(new ArrayList<>());

        mockList.add("toto");
        mockList.add("titi");
        mockList.add("tata");

        Mockito.verify(mockList, Mockito.times(3)).add(Mockito.anyString());
        Mockito.verify(mockList).add("toto");
        Mockito.verify(mockList).add("titi");
        Mockito.verify(mockList).add("tata");

        Assert.assertEquals(3, mockList.size());

        Mockito.doReturn(4).when(mockList).size();
        Assert.assertEquals(4, mockList.size());
    }

    @Test
    public void simpleCaptorList() {
        final List<String> mockList = Mockito.mock(List.class);
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        mockList.add("toto");

        Mockito.verify(mockList).add(captor.capture());

        Assert.assertEquals("toto", captor.getValue());
        Assert.assertEquals(0, mockList.size());

        Mockito.doReturn(4).when(mockList).size();
        Assert.assertEquals(4, mockList.size());
    }
}

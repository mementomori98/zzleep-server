package zzleep;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

// https://www.vogella.com/tutorials/Mockito/article.html

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Object obj;

    @Test
    public void shouldAnswerWithTrue()
    {
        when(obj.toString()).thenReturn("Hello World");
        assertEquals("Hello World", obj.toString());
    }
}

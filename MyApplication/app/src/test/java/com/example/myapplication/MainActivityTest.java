package com.example.myapplication;
import org.junit.Test;


public class MainActivityTest {
    MainActivity mainActivity;

    @Test
    public void testOnCreate() throws Exception {
        mainActivity.onCreate(null);
    }

    @Test
    public void testOnStart() throws Exception {
        mainActivity.onStart();
    }

    @Test
    public void testDisplayInformation() throws Exception {
        mainActivity.displayInformation(null);
    }

    @Test
    public void testReloadAPI() throws Exception {
        mainActivity.reloadAPI(null);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
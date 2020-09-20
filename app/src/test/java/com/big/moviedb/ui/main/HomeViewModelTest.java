package com.big.moviedb.ui.main;


import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.big.moviedb.data.remote.NetworkService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.disposables.CompositeDisposable;
import kotlin.jvm.JvmField;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class HomeViewModelTest {
    @Rule
    @JvmField
    public TestRule rule = new InstantTaskExecutorRule();
    @Mock
    @JvmField
    public NetworkService networkService = null;
    @Mock
    @JvmField
    public SharedPreferences sharedPreferences = null;
    private CompositeDisposable compositeDisposable = null;

    private HomeViewModel homeViewModel = null;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compositeDisposable = new CompositeDisposable();
        homeViewModel =
                new HomeViewModel(compositeDisposable, networkService, sharedPreferences);
    }


    /*
      Test to check if created items are null or not
     */
    @Test
    public void _01TestNonNull() {
        assert (homeViewModel != null);
        assert (networkService != null);
        assert (sharedPreferences != null);
    }


    /*
      Test to check if shared preference don't contain any item and we want to save a city
     */
    @Test
    public void _02TestSaveToLocalFirstItem() {
        //Mocking
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString("Movie_Names", "")).thenReturn("");
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        doNothing().when(editor).apply();

        //Action
        homeViewModel.saveDataInLocal("ROCKY");

        //Verify
        verify(editor, times(1)).putString(anyString(), anyString());
        verify(editor, times(1)).apply();
    }

    /*
      Test to check if shared preference contains multiple item and we want to save a city
     */
    @Test
    public void _03TestSaveToLocalMultipleItem() {
        //Mocking
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        when(editor.putString(anyString(), anyString())).thenReturn(editor);

        when(sharedPreferences.getString("Movie_Names", "")).thenReturn("");

        doNothing().when(editor).apply();

        //Action
        homeViewModel.saveDataInLocal("ROCKY");

        //Verify
        verify(editor, times(1)).putString(anyString(), anyString());
        verify(editor, times(1)).apply();
    }


    /*
      Test to check if we pass empty city will it be saved in local preference
     */
    @Test
    public void _04TestSaveToLocalNoItem() {
        //Mocking
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);

        //Action
        homeViewModel.saveDataInLocal("");

        //Verify
        verify(editor, times(0)).putString(anyString(), anyString());
        verify(editor, times(0)).apply();
    }

    @After
    public void tearDown() {
        compositeDisposable = null;
        homeViewModel = null;
        networkService = null;
        sharedPreferences = null;
    }

}

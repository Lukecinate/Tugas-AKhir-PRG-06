package com.example.project73;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import com.example.project73.activity.MainActivity;
import com.example.project73.fragment.FeedbackListFragment;
import com.example.project73.model.Feedback;
import com.example.project73.mvvm.FeedbackListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class FeedbackListFragmentTest {

    private FeedbackListFragment feedbackListFragment;

    @Mock
    private FeedbackListViewModel feedbackListViewModel;

    @Mock
    private RecyclerView recyclerView;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        feedbackListFragment = new FeedbackListFragment();
        feedbackListFragment.feedbackListViewModel = feedbackListViewModel;
        feedbackListFragment.recyclerView = recyclerView;
    }

    @Test
    public void testUpdateUI() {
        // Create a list of feedback items
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback(/* feedback data */));

        // Create a MutableLiveData object and set its value to the feedback list
        MutableLiveData<List<Feedback>> feedbackLiveData = new MutableLiveData<>();
        feedbackLiveData.setValue(feedbacks);

        // Mock the ViewModel's getFeedbacks() method to return the MutableLiveData
        when(feedbackListViewModel.getFeedbacks()).thenReturn(feedbackLiveData);

        // Call the updateUI() method
        feedbackListFragment.updateUI(feedbacks);

        // Verify that the RecyclerView's adapter is set
        verify(recyclerView).setAdapter(any(FeedbackListFragment.FeedbackAdapter.class));
    }

    @Test
    public void testOnFeedbackSelected() {
        // Create a mock of the Callbacks interface
        FeedbackListFragment.Callbacks callbacks = mock(FeedbackListFragment.Callbacks.class);
        feedbackListFragment.callbacks = callbacks;

        // Create a feedback item
        Feedback feedback = new Feedback(/* feedback data */);

        // Create a FeedbackHolder and bind the feedback item to it
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup parent = new LinearLayout(getContext());
        FeedbackListFragment.FeedbackHolder holder = feedbackListFragment.new FeedbackHolder(inflater, parent);
        holder.bind(feedback);

        // Call the onClick() method of the FeedbackHolder
        holder.onClick(null);

        // Verify that the onFeedbackSelected() method of the Callbacks interface is called
        verify(callbacks).onFeedbackSelected(feedback.getId());
    }


    // Additional tests for other methods of the FeedbackListFragment class can be added here

}

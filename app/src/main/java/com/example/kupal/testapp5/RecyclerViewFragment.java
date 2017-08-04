package com.example.kupal.testapp5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class RecyclerViewFragment extends Fragment {


    //<-------------------------private data members------------------------------------->

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Map<String, ?>> movieList;
    private Toolbar toolbar;
    private LinearLayoutManager lm;
    private String mParam1;
    private String mParam2;
    private Toolbar supportActionBar;
    private MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;

    //<-------------------------public data members------------------------------------->

    public MovieData movieData;
    public OnCardItemClickedListener onCardItemClickedListener;

    public RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;

    //<-------------------------Data members------------------------------------->

    Button selectAll;
    Button clearAll;
    Button delete;
    Button sort;
    int layoutType = 1;


    //<-------------------------new Instance method for fragment to fill activity container------------------------->

    public static RecyclerViewFragment newInstance(String param1, String param2) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //<-------------------------onCreate method for the fragment------------------------------------->

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //onCardItemClickedListener = (OnCardItemClickedListener) getContext();
        setRetainInstance(true);
        onCardItemClickedListener = (OnCardItemClickedListener) getContext();
    }

//<-------------------------onCreate method for the fragment------------------------------------->

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setHasOptionsMenu(true);

        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("moviedata").getRef();
        myFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter(MovieX.class, R.layout.card_item_view, MyFirebaseRecylerAdapter.MovieViewHolder.class
                , childRef, getContext());

        movieData = new MovieData();
        //<<================================CHANGES MADE ABOVE ONLY ===================================>>
        //((AppCompatActivity)getActivity()).getSupportActionBar();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView1);

        //recyclerView.setHasFixedSize(true);
        if (layoutType == 1)
            lm = new LinearLayoutManager(getActivity());
        else
            lm = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(lm);

        //recyclerViewAdapter = new RecyclerViewAdapter(rootView.getContext(), movieData.getMoviesList());
        recyclerView.setAdapter(myFirebaseRecylerAdapter);
        if (movieData.getSize() == 0) {
            movieData.setAdapter(myFirebaseRecylerAdapter);
            movieData.setContext(getActivity());
            movieData.initializeDataFromCloud();
        }

        Toast.makeText(getActivity(),"size : "+ String.valueOf(movieData.getSize()),Toast.LENGTH_LONG).show();
//        HashMap movie = new HashMap();
//        movie.put("id", ("rofl"));
//        movieData.addItemToServer(movie);

        List<Map<String,?>> list = movieData.getMoviesList();


//        final navigateToMovieSelected handlerNavigationListener;
//        try{
//            handlerNavigationListener = (navigateToMovieSelected) rootView.getContext();
//        }catch (ClassCastException ex){
//            throw new ClassCastException("ERROR");
//        }

        adapterAnimation();
        itemAnimator();

        //<-------------------------Click implementations------------------------------------->

        myFirebaseRecylerAdapter.setOnItemClickListener(new MyFirebaseRecylerAdapter.RecyclerItemClickListener() {

            @Override
            public void onItemClick(View v, final int position) {
                if(movieData.getItem(position) != null){
                    onCardItemClickedListener.onCardItemClicked(movieData.getItem(position));
                }
            }



            @Override
            public void onOverFlowButtonClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {

                                                                    @Override
                                                                    public boolean onMenuItemClick(MenuItem item) {
                                                                        HashMap movie;
                                                                        int id = item.getItemId();
                                                                        switch (id) {
                                                                            case R.id.delete:
                                                                                movie = (HashMap) ((HashMap) movieData.getItem(position));
                                                                                  movieData.removeItemFromServer(movie);
                                                                        return true;
                                                                        case R.id.duplicate:
                                                                            movie = (HashMap) ((HashMap) movieData.getItem(position)).clone();
                                                                            movie.put("id", ((String) movie.get("id") + "_new"));
                                                                            movieData.addItemToServer(movie);
                                                                            return true;
                                                                        default:
                                                                            return false;
                                                                        }}
                                                                });
                MenuInflater menuInflater = popup.getMenuInflater();
                menuInflater.inflate(R.menu.contextual_action_bar_menu, popup.getMenu());
                popup.show();
            }
        });

        selectAll = (Button) rootView.findViewById(R.id.selectAll);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < movieData.getSize(); i++) {
                    movieData.getItem(i).put("selection", true);
                    recyclerViewAdapter.notifyItemChanged(i);
                }
            }
        });

        clearAll = (Button) rootView.findViewById(R.id.clearAll);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < movieData.getSize(); i++) {
                    movieData.getItem(i).put("selection", false);
                    recyclerViewAdapter.notifyItemChanged(i);
                }
            }
        });

        delete = (Button) rootView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = movieData.getSize();
//                for (int i = 0; i < size; i++) {
//                    boolean selectFlag = (Boolean) movieData.getItem(i).get("selection");
//                    if (selectFlag) {
//                        movieData.removeItem(i);
//                        recyclerViewAdapter.notifyItemRemoved(i);
//                        i--;
//                        size--;
//                    }
//                }
//
//                if(size<=5)
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        sort = (Button) rootView.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(movieData.getMoviesList(), new Comparator<Map<String, ?>>() {
                    @Override
                    public int compare(Map<String, ?> m1, Map<String, ?> m2) {
                        System.out.println("Enter");
                        return (Integer.parseInt(m2.get("year").toString()) - Integer.parseInt(m1.get("year").toString()));
                    }
                });
                recyclerViewAdapter.check = true;
//                movieData.updateItems(movieList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    //<-------------------------helper methods called by activity action bar / toolbar------------------------------------->

    public void sortByYear() {
        Collections.sort(movieData.getMoviesList(), new Comparator<Map<String, ?>>() {
            @Override
            public int compare(Map<String, ?> m1, Map<String, ?> m2) {
                System.out.println("Enter");
                return (Integer.parseInt(m2.get("year").toString()) - Integer.parseInt(m1.get("year").toString()));
            }
        });
        recyclerViewAdapter.check = true;
//                movieData.updateItems(movieList);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void deleteAllMovies() {
        int size = movieData.getSize();
//        for (int i = 0; i < size; i++) {
//            movieData.removeItem(i);
//            recyclerViewAdapter.notifyItemRemoved(i);
//            i--;
//            size--;
//        }
        if (size <= 5)
            recyclerViewAdapter.notifyDataSetChanged();
    }

    public void changeToGridView() {
        if (layoutType == 1)
            layoutType = 2;
        else if (layoutType == 2)
            layoutType = 1;
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private void itemAnimator() {
        SlideInRightAnimator animator = new SlideInRightAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setAddDuration(1000);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);
    }

    //<-------------------------------------ANIMATION---------------------------------------->

    private void adapterAnimation() {
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(myFirebaseRecylerAdapter);
        alphaAdapter.setDuration(500);
        recyclerView.setAdapter(alphaAdapter);
    }

    public interface OnCardItemClickedListener {
        public void onCardItemClicked(HashMap<String, ?> movie);
    }

    public interface navigateToMovieSelected {
        public void navigateToMovieSelected(int position, HashMap<String, String> movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //<-------------------------override for an option menu inside toolbar/actionbar------------------------->

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.findItem(R.id.actionSearch) == null) {
            inflater.inflate(R.menu.recycler_view_menu, menu);
        }

        SearchView search = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        if (search != null) {
            search.setSubmitButtonEnabled(true);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    int position = movieData.findFirst(query);
                    if (position >= 0) {
                        recyclerView.scrollToPosition(position);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortbyName:
                // do whatever
                Toast.makeText(getActivity(), "Sort by Name!", Toast.LENGTH_LONG).show();
                Collections.sort(movieData.getMoviesList(), new Comparator<Map<String, ?>>() {
                    @Override
                    public int compare(Map<String, ?> m1, Map<String, ?> m2) {
                        System.out.println("Enter");
                        return m1.get("name").toString().compareTo(m2.get("name").toString());
                    }
                });
                recyclerViewAdapter.check = true;
//                movieData.updateItems(movieList);
                recyclerViewAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//<-------------------------CONTEXTUAL MENU IMPLEMENTATION------------------------------------->

    public class ActionBarCallBack implements android.view.ActionMode.Callback {

        int position;

        public ActionBarCallBack(int position) {
            this.position = position;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_action_bar_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            HashMap md = movieData.getItem(position);
            mode.setTitle((String) md.get("name"));
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.delete:
//                    movieData.removeItem(position);
//                    recyclerViewAdapter.notifyItemRemoved(position);
//                    mode.finish();
                    break;
                case R.id.duplicate:
//                    movieData.addItem(position + 1, (HashMap<String, ?>) movieData.getItem(position).clone());
//                    recyclerViewAdapter.notifyItemInserted(position + 1);
//                    mode.finish();
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
//<------------------------------------------End--------------------------------------------------->

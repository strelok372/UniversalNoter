package ru.dozorov.notesanddates.adapters;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;
import ru.dozorov.notesanddates.fragments.DateNotesFragment;
import ru.dozorov.notesanddates.fragments.SimpleNotesFragment;
import ru.dozorov.notesanddates.fragments.ToDoListFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<String>();
        //    Fragment[] fragments = {new DateNotesFragment(), new SimpleNotesFragment(), new ToDoListFragment()};

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments.add(new DateNotesFragment());
        fragments.add(new SimpleNotesFragment());
        fragments.add(new ToDoListFragment());
        titles.add("Date Notes");
        titles.add("Simple Notes");
        titles.add("To Do List");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

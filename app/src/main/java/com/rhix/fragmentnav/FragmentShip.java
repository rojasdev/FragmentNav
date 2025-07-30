package com.rhix.fragmentnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FragmentShip extends Fragment {
    private ListView timelineList;

    public FragmentShip() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ship, container, false);

        timelineList = root.findViewById(R.id.timelineList);

        ArrayList<TimelineItem> data = loadTimelineFromAssets("timeline.json");
        TimelineAdapter adapter = new TimelineAdapter(requireContext(), R.layout.timeline_layout, data);
        timelineList.setAdapter(adapter);

        return root;
    }

    private ArrayList<TimelineItem> loadTimelineFromAssets(String filename) {
        ArrayList<TimelineItem> list = new ArrayList<>();
        try {
            String json = readAssetText(filename);
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                list.add(new TimelineItem(
                        o.optString("title"),
                        o.optString("description"),
                        o.optString("timestamp"),
                        o.optString("icon")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String readAssetText(String filename) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = requireContext().getAssets().open(filename);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

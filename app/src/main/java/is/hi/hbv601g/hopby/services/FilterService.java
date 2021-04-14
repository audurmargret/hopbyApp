package is.hi.hbv601g.hopby.services;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class FilterService {

    public boolean[] getBoolean(ChipGroup chipGroup){
        int size = chipGroup.getChildCount();
        boolean[] list = new boolean[size];

        for (int i=0; i<chipGroup.getChildCount();i++){
            Chip chip = (Chip)chipGroup.getChildAt(i);
            list[i] = chip.isChecked();
        }
        return list;
    }
}

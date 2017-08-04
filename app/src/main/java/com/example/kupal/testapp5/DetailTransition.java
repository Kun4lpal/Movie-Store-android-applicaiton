package com.example.kupal.testapp5;

import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * Created by kupal on 2/19/2017.
 */

public class DetailTransition extends TransitionSet{
    void DetailTransition(){
        setOrdering(ORDERING_TOGETHER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addTransition(new ChangeBounds())
                    .addTransition(new ChangeTransform())
                    .addTransition(new ChangeImageTransform());
        }
    }
}

//<--------------------------------------------- END ------------------------------------------->

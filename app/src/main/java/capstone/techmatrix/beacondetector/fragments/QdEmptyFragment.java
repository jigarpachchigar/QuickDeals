package capstone.techmatrix.beacondetector.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import capstone.techmatrix.beacondetector.R;


public class QdEmptyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.blank, container, false);
    }

    @Override
    public Animation onCreateAnimation (int transit, boolean enter, int nextAnim) {

        //Check if the superclass already created the animation
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);

        //If not, and an animation is defined, load it now
        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        //If there is an animation for this fragment, add a listener.
        if (anim != null) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart (Animation animation) {
                    onAnimationStarted();
                }

                @Override
                public void onAnimationEnd (Animation animation) {
                    onAnimationEnded();
                }

                @Override
                public void onAnimationRepeat (Animation animation) {
                    onAnimationRepeated();
                }
            });
        }
        return anim;
    }

    protected void onAnimationStarted () {}

    @SuppressWarnings("ConstantConditions")
    protected void onAnimationEnded () {
        // Remove Fragment
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fm.findFragmentById(R.id.fragment));
        ft.commit();
    }

    protected void onAnimationRepeated () {}
}

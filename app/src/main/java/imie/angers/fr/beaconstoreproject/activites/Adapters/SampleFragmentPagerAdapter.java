package imie.angers.fr.beaconstoreproject.activites.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import imie.angers.fr.beaconstoreproject.activites.ListPromoBanniere;
import imie.angers.fr.beaconstoreproject.activites.ListPromoBeaconActivity;
import imie.angers.fr.beaconstoreproject.activites.Panier;

/**
 * Created by Anne on 06/03/2016.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "NEWS", "PANIER", "PROMOTIONS" };

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return ListPromoBanniere.newInstance(position + 1);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return Panier.newInstance(position + 1);
            case 2:
                return ListPromoBeaconActivity.newInstance(position + 1);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}
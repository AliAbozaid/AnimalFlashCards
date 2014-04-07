package com.example.animalflashcards;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * This page transformer will create the effect that each page is an edge of a regular polygon
 * that is rotated to show each page. Can only be used on devices with Honeycomb or higher api
 * <p>
 * Example usage:
 * <p>
 * <pre> 
 * if(BUILD.VERSION.SDK_INT >= BUILD.VERSION_CODES.HONEYCOMB){
 * 	mPager.setPageTransformer(true, new RotationPageTransformer(DEGREES_BETWEEN_CARDS));
 * 	mPager.setOffscreenPageLimit(mPagerAdapter.getCount());
 * 	mPager.setPageMargin(2 * paddingOnPages);
 * 	mPager.setClipChildren(false);
 * }
</pre>
 * <p>
 * @author Steven Kideckel
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RotationPageTransformer implements ViewPager.PageTransformer{
	private float minAlpha;
	private int degrees;
	private float distanceToCentreFactor;
	
	/**
	 * Creates a RotationPageTransformer
	 * @param degrees the inner angle between two edges in the "polygon" that the pages are on.
	 * An acute angle means that the pages are on the inside of the shape and an obtuse angle means that they are
	 * on the outside of the shape
	 */
	public RotationPageTransformer(int degrees){
		this(degrees, 0.7f);
	}
	
	/**
	 * Creates a RotationPageTransformer
	 * @param degrees the inner angle between two edges in the "polygon" that the pages are on.
	 * An acute angle means that the pages are on the inside of the shape and an obtuse angle means that they are
	 * on the outside of the shape
	 * @param minAlpha the least faded out that the side 
	 */
	public RotationPageTransformer(int degrees, float minAlpha){
		this.degrees = degrees;
		distanceToCentreFactor = (float) Math.tan(Math.toRadians(degrees / 2))/2;
		this.minAlpha = minAlpha;
	}
	
	public void transformPage(View view, float position){
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();
		view.setPivotX((float) pageWidth / 2);
		view.setPivotY((float) (pageHeight + pageWidth * distanceToCentreFactor)); 
		
		if(position < -1){ //[-infinity,1)
			//off to the left by a lot
			view.setRotation(0);
			view.setAlpha(0);
		}else if(position <= 1){ //[-1,1]
			view.setTranslationX((-position) * pageWidth);
			view.setRotation(position * (180 - degrees));
			
			// Fade the page relative to its distance from the center
			view.setAlpha(Math.max(minAlpha, 1 - Math.abs(position)/3));
		}else{ //(1, +infinity]
			//off to the right by a lot
			view.setRotation(0);
			view.setAlpha(0);
		}
	}	
}
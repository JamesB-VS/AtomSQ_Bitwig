// Written by James Bell
// (c) 2023
// Licensed under GPLv3 - https://www.gnu.org/licenses/gpl-3.0.txt
package com.presonus.handler;

import com.bitwig.extension.controller.api.*;
import com.presonus.AtomSQExtension;

public class BrowserHandler
{
    public PopupBrowser mPopupBrowser;
    public CursorBrowserResultItem mBrowserResult;

    private CursorBrowserFilterItem mBrowserCategory;
    private CursorBrowserFilterItem mBrowserCreator;
    private CursorBrowserFilterItem mBrowserTag;
    private CursorBrowserFilterItem mBrowserFavorites;
    private CursorBrowserFilterItem mBrowserDevType;
    private CursorBrowserFilterItem mBrowserLocation;
    private CursorBrowserFilterItem mBrowserFileType;

    private HardwareActionBindable inc0, dec0, inc1, dec1, inc2, dec2, inc3, dec3;
    private HardwareActionBindable inc4, dec4, inc5, dec5, inc6, dec6, inc7, dec7;

    public RelativeHardwarControlBindable RHCBsmartfolders, RHCBtags, RHCBcreator;
    public RelativeHardwarControlBindable RHCBcategory, RHCBdevices, RHCBlocations;
    public RelativeHardwarControlBindable RHCBfiletype, RHCBresult, RHCBnothing;

    private ControllerHost mHost;
    private CursorDevice mCursorDevice;

    public void start(AtomSQExtension ext)
    {
        mHost = ext.mHost;
        mCursorDevice = ext.mCursorDevice;

        mPopupBrowser = mHost.createPopupBrowser();
        mPopupBrowser.exists().markInterested();
        mPopupBrowser.selectedContentTypeIndex().markInterested();
        mPopupBrowser.contentTypeNames().markInterested();
        mPopupBrowser.selectedContentTypeIndex().markInterested();
        mPopupBrowser.selectedContentTypeName().markInterested();

        mBrowserResult = (CursorBrowserResultItem) mPopupBrowser.resultsColumn().createCursorItem();
        mBrowserCategory = (CursorBrowserFilterItem) mPopupBrowser.categoryColumn().createCursorItem();
        mBrowserCreator = (CursorBrowserFilterItem) mPopupBrowser.creatorColumn().createCursorItem();
        mBrowserTag = (CursorBrowserFilterItem) mPopupBrowser.tagColumn().createCursorItem();
        mBrowserResult.exists().markInterested();
        mBrowserCategory.exists().markInterested();
        mBrowserCreator.exists().markInterested();
        mBrowserTag.exists().markInterested();
        mBrowserResult.name().markInterested();

        //V1.1 Preset Browser
        mBrowserLocation = (CursorBrowserFilterItem) mPopupBrowser.locationColumn().createCursorItem();
        mBrowserLocation.exists().markInterested();
        mBrowserFileType = (CursorBrowserFilterItem) mPopupBrowser.fileTypeColumn().createCursorItem();
        mBrowserFileType.exists().markInterested();
        mBrowserFavorites = (CursorBrowserFilterItem) mPopupBrowser.smartCollectionColumn().createCursorItem();
        mBrowserFavorites.exists().markInterested();
        mBrowserDevType = (CursorBrowserFilterItem) mPopupBrowser.deviceColumn().createCursorItem();
        mBrowserDevType.exists().markInterested();

        createBrowserTargets();
    }

    private void createBrowserTargets()
    {
        inc0 = mHost.createAction(() -> mBrowserFavorites.selectNext(),    () -> "+");
        dec0 = mHost.createAction(() -> mBrowserFavorites.selectPrevious(), () -> "-");
        RHCBsmartfolders = mHost.createRelativeHardwareControlStepTarget(inc0, dec0);

        inc1 = mHost.createAction(() -> mBrowserDevType.selectNext(),    () -> "+");
        dec1 = mHost.createAction(() -> mBrowserDevType.selectPrevious(), () -> "-");
        RHCBdevices = mHost.createRelativeHardwareControlStepTarget(inc1, dec1);

        inc2 = mHost.createAction(() -> mBrowserLocation.selectNext(),    () -> "+");
        dec2 = mHost.createAction(() -> mBrowserLocation.selectPrevious(), () -> "-");
        RHCBlocations = mHost.createRelativeHardwareControlStepTarget(inc2, dec2);

        inc3 = mHost.createAction(() -> mBrowserFileType.selectNext(),    () -> "+");
        dec3 = mHost.createAction(() -> mBrowserFileType.selectPrevious(), () -> "-");
        RHCBfiletype = mHost.createRelativeHardwareControlStepTarget(inc3, dec3);

        inc4 = mHost.createAction(() -> mBrowserCategory.selectNext(),    () -> "+");
        dec4 = mHost.createAction(() -> mBrowserCategory.selectPrevious(), () -> "-");
        RHCBcategory = mHost.createRelativeHardwareControlStepTarget(inc4, dec4);

        inc5 = mHost.createAction(() -> mBrowserTag.selectNext(),    () -> "+");
        dec5 = mHost.createAction(() -> mBrowserTag.selectPrevious(), () -> "-");
        RHCBtags = mHost.createRelativeHardwareControlStepTarget(inc5, dec5);

        inc6 = mHost.createAction(() -> mBrowserCreator.selectNext(),    () -> "+");
        dec6 = mHost.createAction(() -> mBrowserCreator.selectPrevious(), () -> "-");
        RHCBcreator = mHost.createRelativeHardwareControlStepTarget(inc6, dec6);

        inc7 = mHost.createAction(() -> mBrowserResult.selectNext(),    () -> "+");
        dec7 = mHost.createAction(() -> mBrowserResult.selectPrevious(), () -> "-");
        RHCBresult = mHost.createRelativeHardwareControlStepTarget(inc7, dec7);

        RHCBnothing = mHost.createRelativeHardwareControlStepTarget(null, null);
    }

    // Opens the browser at the correct insertion point: replaces the current device if one exists,
    // otherwise adds to the end of the device chain on an empty track.
    public void startPresetBrowsing()
    {
        if (mCursorDevice.exists().get())
        {
            mCursorDevice.replaceDeviceInsertionPoint().browse();
        }
        else
        {
            mCursorDevice.deviceChain().endOfDeviceChainInsertionPoint().browse();
        }
    }
}

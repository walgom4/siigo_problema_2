/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hackathon.retodos.presentation.ui.camera;

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.hackathon.retodos.domain.ui.camera.GraphicOverlay;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private OcrDetectorListener ocrDetectorListener;
    private OcrDetectorListenerTextBlock ocrDetectorListenerTextBlock;

    private String[] eps;
    private String[] epsReal;

    public interface OcrDetectorListener{
        @UiThread
        void onOcrDetected(String name, String value);
    }

    public interface OcrDetectorListenerTextBlock{
        @UiThread
        void onOcrDetectedTextBlock(String name, TextBlock value);
    }
    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, Context context) {
        mGraphicOverlay = ocrGraphicOverlay;
        if (context instanceof  OcrDetectorListener) {
            this.ocrDetectorListener= (OcrDetectorListener) context;
        } else {
            throw new RuntimeException("Hosting activity must implement BarcodeUpdateListener");
        }
        if (context instanceof  OcrDetectorListenerTextBlock) {
            this.ocrDetectorListenerTextBlock= (OcrDetectorListenerTextBlock) context;
        } else {
            throw new RuntimeException("Hosting activity must implement BarcodeUpdateListener");
        }


    }



    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            ocrDetectorListenerTextBlock.onOcrDetectedTextBlock("values", item);
            String value = items.valueAt(i).getValue();
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}

package mbn.libs.imagelibs.imageworks;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

public class Effects3 {

    public static Bitmap motionBlurPainting(Bitmap src, float influenceFactor, int preBlurPasses, int edgeDetectRadios, int kernelSideSize) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = Effects.getPixels(src);
        pixels = motionBlurPainting(pixels, width, height, influenceFactor, preBlurPasses, edgeDetectRadios, kernelSideSize);
        return Effects.createAndSetPixels(pixels, width, height);
    }

    public static Bitmap motionBlurPainting_reverse(Bitmap src, int preBlurPasses, int edgeDetectRadios, int kernelSideSize) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = Effects.getPixels(src);
        pixels = motionBlurPainting_reverse(pixels, width, height, preBlurPasses, edgeDetectRadios, kernelSideSize);
        return Effects.createAndSetPixels(pixels, width, height);
    }

    public static int[] motionBlurPainting(int[] pixels, int width, int height, float influenceFactor, int preBlurPasses, int edgeDetectRadios, int kernelSideSize) {
        if (kernelSideSize % 2 == 0 || kernelSideSize < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] motionValues = new float[pixels.length];
        int[] edgePixels = Effects.boxFiltering_forkJoin(preBlurPasses, pixels, width, height, Effects.BOX_BLUR_KERNEL);
        Effects.getNegativePixels(edgePixels);
        edgePixels = Effects.boxFiltering_forkJoin(edgePixels, width, height, Effects.createGaussianEdgeDetectKernel(edgeDetectRadios));

        for (int i = 0; i < edgePixels.length; i++) {
            int color = edgePixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float prP = maxPart / 255f;

//            if (prP <= 0.2f) {
//                edgePixels[i] = 0;
//                continue;
//            }
//                prP *= 10f;
            prP = prP > 1f ? 1 : prP;
            motionValues[i] = prP;
        }
        edgePixels = null;
        int edgeWidth = kernelSideSize / 2;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;
        float[] kernel = new float[kernelSideSize * kernelSideSize];
        int[] indices = Effects.indexCreator(kernelSideSize, width);
        float denominator = 0;
//        int[] newPixels = new int[pixels.length];
        int[] newPixels = Arrays.copyOf(pixels, pixels.length);

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;

                for (int k = 0; k < indices.length; k++) {
                    kernel[k] = motionValues[indexOffset + indices[k]] * influenceFactor;
                }
                kernel[kernel.length / 2] = 1f;
                denominator = Effects.calculateDenominator(kernel);

                for (int k = 0; k < indices.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
                }
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                newPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
        return newPixels;
    }

    public static int[] motionBlurPainting_reverse(int[] pixels, int width, int height, int preBlurPasses, int edgeDetectRadios, int kernelSideSize) {
        if (kernelSideSize % 2 == 0 || kernelSideSize < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] motionValues = new float[pixels.length];
        int[] edgePixels = Arrays.copyOf(pixels, pixels.length);
        edgePixels = Effects.boxFiltering(preBlurPasses, edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
        Effects.getNegativePixels(edgePixels);
//        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createEdgeDetectKernel(edgeDetectRadios));
        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createGaussianEdgeDetectKernel(edgeDetectRadios));

        for (int i = 0; i < edgePixels.length; i++) {
            int color = edgePixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float prP = maxPart / 255f;

//            if (prP <= 0.05f) {
//                edgePixels[i] = 0;
//                continue;
//            }
//                prP *= 10f;
            prP = prP > 1f ? 1 : prP;
            motionValues[i] = 1 - prP;
        }
        edgePixels = null;
        int edgeWidth = kernelSideSize / 2;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;
        float[] kernel = new float[kernelSideSize * kernelSideSize];
        int[] indices = Effects.indexCreator(kernelSideSize, width);
        float denominator = 0;
//        int[] newPixels = new int[pixels.length];
        int[] newPixels = Arrays.copyOf(pixels, pixels.length);

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;

                for (int k = 0; k < indices.length; k++) {
                    kernel[k] = motionValues[indexOffset + indices[k]];
                }
                kernel[kernel.length / 2] = 1f;
                denominator = Effects.calculateDenominator(kernel);

                for (int k = 0; k < indices.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
                }
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                newPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
        return newPixels;
    }


    public static Bitmap dynamicMotionBlurPainting(Bitmap src, int preBlurPasses, int edgeDetectRadios, int maxKernelSide) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = Effects.getPixels(src);
        pixels = dynamicMotionBlurPainting(pixels, width, height, preBlurPasses, edgeDetectRadios, maxKernelSide);
        return Effects.createAndSetPixels(pixels, width, height);
    }

    public static int[] dynamicMotionBlurPainting(int[] pixels, int width, int height, int preBlurPasses, int edgeDetectRadios, int maxKernelSideSize) {
        if (maxKernelSideSize % 2 == 0 || maxKernelSideSize < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] motionValues = new float[pixels.length];
        int[] edgePixels = Arrays.copyOf(pixels, pixels.length);
        edgePixels = Effects.boxFiltering_forkJoin(preBlurPasses, edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
        Effects.getNegativePixels(edgePixels);
        edgePixels = Effects.boxFiltering_forkJoin(edgePixels, width, height, Effects.createGaussianEdgeDetectKernel(edgeDetectRadios));

        for (int i = 0; i < edgePixels.length; i++) {
            int color = edgePixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float prP = maxPart / 255f;

            prP = Math.min(prP, 1f);
            motionValues[i] = 1 - prP;
        }
        edgePixels = null;
        Object[] k_i = getEmptyKernelsAndIndices(width, maxKernelSideSize);
        int[][] indicesArray = (int[][]) k_i[0];
        float[][] kernelsArray = (float[][]) k_i[1];
        float arraysLength = Math.nextAfter(indicesArray.length, Float.NEGATIVE_INFINITY);
        k_i = null;
        int edgeWidth = maxKernelSideSize / 2;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;
        float[] kernel;
        int[] indices;
        float pr;
        float denominator = 0;
        int[] newPixels = Arrays.copyOf(pixels, pixels.length);

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;
                pr = motionValues[indexOffset];
                indices = indicesArray[(int) (arraysLength * pr)];
                kernel = kernelsArray[(int) (arraysLength * pr)];

                for (int k = 0; k < indices.length; k++) {
                    kernel[k] = motionValues[indexOffset + indices[k]];
                }
                kernel[kernel.length / 2] = 1f;
                denominator = Effects.calculateDenominator(kernel);

                for (int k = 0; k < indices.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
                }
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                newPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
        return newPixels;
    }


    private static Object[] getEmptyKernelsAndIndices(int imageWidth, int maxSide) {
        int num = (int) Math.ceil((maxSide - 2f) / 2f);
        int[][] indicesArray = new int[num][];
        float[][] kernelsArray = new float[num][];
        int count = 0;
        for (int i = 3; i <= maxSide; i += 2) {
            indicesArray[count] = Effects.indexCreator(i, imageWidth);
            kernelsArray[count] = new float[i * i];
            count++;
        }
        return new Object[]{indicesArray, kernelsArray};
    }

    public static Bitmap dynamicPixelFlowPainting(Bitmap src, float threshold, int maxKernelSide) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = Effects.getPixels(src);
        pixels = dynamicPixelFlowPainting(pixels, width, height, threshold, maxKernelSide);
        return Effects.createAndSetPixels(pixels, width, height);
    }

    public static int[] dynamicPixelFlowPainting(int[] pixels, int width, int height, float threshold, int maxKernelSideSize) {
        if (maxKernelSideSize % 2 == 0 || maxKernelSideSize < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] motionValues = new float[pixels.length];
        int[] edgePixels = Arrays.copyOf(pixels, pixels.length);
        edgePixels = Effects.boxFiltering(2, edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
        Effects.getNegativePixels(edgePixels);
        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createEdgeDetectKernel(5));
//        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createGaussianEdgeDetectKernel(7));

        for (int i = 0; i < edgePixels.length; i++) {
            int color = edgePixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float prP = maxPart / 255f;

//            if (prP <= 0.07f) {
//                motionValues[i] = 0;
//                continue;
//            }
//                prP *= 10f;
            prP = Math.min(prP, 1f);
            motionValues[i] = prP;
        }
        edgePixels = null;
        Object[] k_i = getEmptyKernelsAndIndices(width, maxKernelSideSize);
        int[][] indicesArray = (int[][]) k_i[0];
        float[][] kernelsArray = (float[][]) k_i[1];
//        float arraysLength = Math.nextAfter(indicesArray.length, Float.NEGATIVE_INFINITY);
        k_i = null;
        int edgeWidth = maxKernelSideSize / 2;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;
        float[] kernel;
        int[] indices;
        float denominator = 0;
        int pixelInfo;
        byte interestDistance, interestX, interestY;
        double flowPointDistance, currentIndexDistance;
//        int[] newPixels = new int[pixels.length];
        int[] newPixels = Arrays.copyOf(pixels, pixels.length);

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;
                pixelInfo = getPixelFlowInfo(motionValues, width, maxKernelSideSize, j, i, indexOffset, threshold);
                if (pixelInfo == 0xff000000) {
                    continue;
                }
                if (pixelInfo == 7) {
                    indices = indicesArray[(maxKernelSideSize - 2) / 2];
                    kernel = kernelsArray[(maxKernelSideSize - 2) / 2];
                    Arrays.fill(kernel, 1);
                } else {
                    interestX = (byte) ((pixelInfo & 0xff0000) >> 16);
                    interestY = (byte) ((pixelInfo & 0xff00) >> 8);
                    interestDistance = (byte) Math.max(Math.abs(interestX), Math.abs(interestY));
                    flowPointDistance = Math.sqrt(Math.pow(interestX, 2) + Math.pow(interestY, 2));

//                    indices = indicesArray[interestDistance - 1];
                    indices = indicesArray[(maxKernelSideSize - 2) / 2];
//                    kernel = kernelsArray[interestDistance - 1];
                    kernel = kernelsArray[(maxKernelSideSize - 2) / 2];


                    for (int k = 0; k < indices.length; k++) {
                        currentIndexDistance = Math.sqrt(Math.pow(Math.floor((indexOffset + indices[k]) / (width)) - (i + interestY), 2) + Math.pow(((indexOffset + indices[k]) % (width)) - (j + interestX), 2));
                        kernel[k] = (float) (currentIndexDistance / flowPointDistance);
                    }

                    kernel[kernel.length / 2] = 1f;
                }
                denominator = Effects.calculateDenominator(kernel);

                for (int k = 0; k < indices.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
                }
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                newPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
        return newPixels;
    }


    private static int getPixelFlowInfo(float[] edges, int imageWidth, int side, int px, int py, int indexOffset, float threshold) {
//        if (edges[indexOffset] >= 0.9) {
//            return 0xff000000;
//        }
        int halfSide = side / 2;
        byte minX = 0;
        byte minY = 0;
        float currentEdge;
        double currentDistance;
        float currentThreshold = 0;
        double minDistance = Math.sqrt(2 * Math.pow(halfSide, 2));
//        double minDistance = 0;
        for (int y = py - halfSide; y < py + halfSide; y++) {
            for (int x = px - halfSide; x < px + halfSide; x++) {
                currentEdge = edges[(y * imageWidth) + x];
                if (currentEdge >= threshold && currentEdge > currentThreshold) {
//                    currentDistance = Math.sqrt(Math.pow((y - py), 2) + Math.pow((x - px), 2));
//                    if (currentDistance <= minDistance) {
                    minX = (byte) (x - px);
                    minY = (byte) (y - py);
//                        minDistance = currentDistance;
                    currentThreshold = currentEdge;
//                    }
                }
            }
        }


        if (minX == minY && minY == 0 && currentThreshold == 0) {
            return 7;
        } else {
            return ((minX << 16) | (minY << 8));
        }
    }


    public static Bitmap segmentedPainting(Bitmap src,/* 4 is recommended */ int segments, int preBlurPasses, int edgeDetectRadios, int kernelSideSize) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = Effects.getPixels(src);
        pixels = segmentedPainting(pixels, width, height, segments, preBlurPasses, edgeDetectRadios, kernelSideSize);
        return Effects.createAndSetPixels(pixels, width, height);
    }


    public static int[] segmentedPainting(int[] pixels, int width, int height, int segmentsCount, int preBlurPasses, int edgeDetectRadios, int kernelSideSize) {
        if (kernelSideSize % 2 == 0 || kernelSideSize < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] motionValues = new float[pixels.length];
        int[] edgePixels = Effects.boxFiltering(preBlurPasses, pixels, width, height, Effects.BOX_BLUR_KERNEL);
        Effects.getNegativePixels(edgePixels);
        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createGaussianEdgeDetectKernel(edgeDetectRadios));

        for (int i = 0; i < edgePixels.length; i++) {
            int color = edgePixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float prP = maxPart / 255f;

//            if (prP < 1f) {
//                edgePixels[i] = 0;
//                continue;
//            }
//                prP *= 10f;
            prP = prP > 1f ? 1 : prP;
            motionValues[i] = prP;
        }
        edgePixels = null;
        byte[][] segments = new byte[segmentsCount][pixels.length];
//        int[] segmentsPixels = dynamicMotionBlurPainting(pixels, width, height, 1, 3, 7);
        int[] segmentsPixels = Effects.boxFiltering(3, pixels, width, height, Effects.BOX_BLUR_KERNEL);
        float dividerFactor = Math.nextAfter(segmentsCount, Float.NEGATIVE_INFINITY);
        float pr;
        for (int i = 0; i < segmentsPixels.length; i++) {
            pr = (Effects.getMonochromePercentage(segmentsPixels[i])) * dividerFactor;
            segments[(int) pr][i] = 1;
        }
        int edgeWidth = kernelSideSize / 2;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;
        float[] kernel = new float[kernelSideSize * kernelSideSize];
        int[] indices = Effects.indexCreator(kernelSideSize, width);
        float denominator = 0;
//        int[] newPixels = new int[pixels.length];
        int[] newPixels = Arrays.copyOf(pixels, pixels.length);
        for (byte[] segment : segments) {
            for (int i = edgeWidth; i < height - (edgeWidth); i++) {
                for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                    indexOffset = (i * width) + j;
                    if (segment[indexOffset] != 1) {
                        continue;
                    }
                    red = green = blue = 0.0f;
                    for (int k = 0; k < indices.length; k++) {
                        if (segment[indexOffset + indices[k]] == 1) {
                            kernel[k] = motionValues[indexOffset + indices[k]];
                        } else {
                            kernel[k] = 0.05f;
                        }
                    }
                    kernel[kernel.length / 2] = 1f;
                    denominator = Effects.calculateDenominator(kernel);

                    for (int k = 0; k < indices.length; k++) {
                        rgb = pixels[indexOffset + indices[k]];
                        red += ((rgb & 0xff0000) >> 16) * kernel[k];
                        green += ((rgb & 0xff00) >> 8) * kernel[k];
                        blue += (rgb & 0xff) * kernel[k];
                    }
                    ired = (int) (red / denominator);
                    igreen = (int) (green / denominator);
                    iblue = (int) (blue / denominator);
                    if (ired > 0xff) ired = 0xff;
                    else if (ired < 0) ired = 0;
                    if (igreen > 0xff) igreen = 0xff;
                    else if (igreen < 0) igreen = 0;
                    if (iblue > 0xff) iblue = 0xff;
                    else if (iblue < 0) iblue = 0;
                    newPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
                            ((igreen << 8) & 0xff00) | (iblue & 0xff);
                }
            }
        }
        return newPixels;
    }


}

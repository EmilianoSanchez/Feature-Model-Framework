package com.emiliano.samples.faceDetectionAPIs;

public class FaceLandmark {
	public enum LandmarkType{
		unknown,
		pupilLeft,
        pupilRight,
        noseTip,
        mouthLeft,
        mouthRight,
        eyebrowLeftOuter,
        eyebrowLeftInner,
        eyeLeftOuter,
        eyeLeftTop,
        eyeLeftBottom,
        eyeLeftInner,
        eyebrowRightInner,
        eyebrowRightOuter,
        eyeRightInner,
        eyeRightTop,
        eyeRightBottom,
        eyeRightOuter,
        noseRootLeft,
        noseRootRight,
        noseLeftAlarTop,
        noseRightAlarTop,
        noseLeftAlarOutTip,
        noseRightAlarOutTip,
        upperLipTop,
        upperLipBottom,
        underLipTop,
        underLipBottom
	}
	
	public FaceLandmark(double x, double y){
		this.type=LandmarkType.unknown;
		this.x=x;
		this.y=y;
	}
	
	public FaceLandmark(LandmarkType type, double x, double y){
		this.type=type;
		this.x=x;
		this.y=y;
	}
	
	public LandmarkType type;
	public double x;
	public double y;
}

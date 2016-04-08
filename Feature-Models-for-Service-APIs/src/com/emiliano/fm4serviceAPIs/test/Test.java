package com.emiliano.fm4serviceAPIs.test;

import com.emiliano.fm4serviceAPIs.RESTRequestApacheClient;
import com.emiliano.fm4serviceAPIs.RESTResponse;
import com.emiliano.fm4serviceAPIs.requestBuilder.SetContentStringAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.AddHeaderAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.SetHostAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.SetMethodAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.AddPathAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.AddQueryParameterAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.RequestBuilder;
import com.emiliano.fm4serviceAPIs.requestBuilder.SetSchemaAction;
import com.emiliano.fm4serviceAPIs.requestBuilder.RequestBuilder.Method;
import com.emiliano.fmframework.building.FMBuilder;
import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.FeatureModelImpl;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.core.constraints.AssignedValue;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.Imply;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.reasoning.ConfOperations;

/**
 * @author Usuarioç
 *
 */
public class Test {

	public static void main(String[] args) {
//		FeatureModel myFm=FMInputOutput.open("/fm1.json");
//		FMInputOutput.save("/fm2.json",myFm);
		
		FeatureModel model= buildFaceRectRequestFM();
		
		Configuration conf=ConfOperations.getPartialConfiguration(model);
		ConfOperations.assignFeature(conf, "GET", FeatureState.SELECTED);
		ConfOperations.assignFeature(conf, "Content", FeatureState.DESELECTED);
		ConfOperations.assignFeature(conf, "Features", FeatureState.DESELECTED);
		System.out.println(conf.toString());
		
		RequestBuilder request=new RESTRequestApacheClient();
		ConfOperations.applyConfiguration(conf, request);
		System.out.println(request);
		RESTResponse response=request.execute();
		
		System.out.println(response);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static FeatureModel buildFaceRectRequestFM(){
			
		FeatureModel faceRect=new FeatureModelImpl();
		
		faceRect.addFeature(new Feature("FaceRect API"));//0
		
		faceRect.addFeature(new Feature("Schema"));//1
		faceRect.addTreeConstraint(new MandatoryFeature("FaceRect API","Schema"));

		faceRect.addFeature(new Feature("Host"));//2
		faceRect.addTreeConstraint(new MandatoryFeature("FaceRect API","Host"));
		
		faceRect.addFeature(new Feature("Path"));//3
		faceRect.addTreeConstraint(new MandatoryFeature("FaceRect API","Path"));
		
		faceRect.addFeature(new Feature("Method"));//4
		faceRect.addTreeConstraint(new MandatoryFeature("FaceRect API","Method"));
		
		faceRect.addFeature(new Feature("Query parameters"));//5
		faceRect.addTreeConstraint(new OptionalFeature("FaceRect API","Query parameters"));
		
		faceRect.addFeature(new Feature("Headers"));//6
		faceRect.addTreeConstraint(new MandatoryFeature("FaceRect API","Headers"));
		
		faceRect.addFeature(new Feature("Content"));//7
		faceRect.addTreeConstraint(new OptionalFeature("FaceRect API","Content"));
		
		faceRect.addFeature(new Feature("https",new SetSchemaAction("https")));//8
		faceRect.addTreeConstraint(new MandatoryFeature("Schema","https"));
		
		faceRect.addFeature(new Feature("apicloud-facerect.p.mashape.com",new SetHostAction("apicloud-facerect.p.mashape.com")));//9
		faceRect.addTreeConstraint(new MandatoryFeature("Host","apicloud-facerect.p.mashape.com"));
		
		faceRect.addFeature(new Feature("process-file.json",new AddPathAction("process-file.json")));//10
		faceRect.addFeature(new Feature("process-url.json",new AddPathAction("process-url.json")));//11
		faceRect.addTreeConstraint(new AlternativeGroup("Path","process-file.json","process-url.json"));
		
		faceRect.addFeature(new Feature("GET",new SetMethodAction(Method.GET)));//12
		faceRect.addFeature(new Feature("POST",new SetMethodAction(Method.POST)));//13
		faceRect.addTreeConstraint(new AlternativeGroup("Method", "GET","POST"));
		
		faceRect.addFeature(new Feature("Features"));//14
		faceRect.addTreeConstraint(new OptionalFeature("Query parameters","Features"));
		
		faceRect.addFeature(new Feature("Image url"));//15
		faceRect.addTreeConstraint(new OptionalFeature("Query parameters","Image url"));
		
		faceRect.addFeature(new Feature("X-Mashape-Key"));//16
		faceRect.addTreeConstraint(new MandatoryFeature("Headers","X-Mashape-Key"));
		
		faceRect.addFeature(new Feature("Form encoded parameters"));//17
		faceRect.addTreeConstraint(new MandatoryFeature("Content","Form encoded parameters"));
		
		faceRect.addFeature(new Feature("features=true",new AddQueryParameterAction("features","true")));//18
		faceRect.addTreeConstraint(new MandatoryFeature("Features","features=true"));
		
		faceRect.addFeature(new Feature("url=...",new AddQueryParameterAction("url","http://apicloud.me/assets/facerect/image1.jpg")));//19
		faceRect.addTreeConstraint(new MandatoryFeature("Image url","url=..."));		
		
		faceRect.addFeature(new Feature("X-Mashape-Key",new AddHeaderAction("X-Mashape-Key","KHSgHKCPshmshHGTwHV3MZPt1Zi1p1R6sM8jsnUc5ioRKTo3qk")));//20
		faceRect.addTreeConstraint(new MandatoryFeature("X-Mashape-Key","X-Mashape-Key"));
		
		faceRect.addFeature(new Feature("image={...}",new SetContentStringAction("image={IMAGE_BINARY}")));//21
		faceRect.addTreeConstraint(new MandatoryFeature("Form encoded parameters","image={...}"));
		
		faceRect.addCrossTreeConstraint(new Imply("GET", "process-url.json"));
		faceRect.addCrossTreeConstraint(new Imply("POST", "process-file.json"));
		faceRect.addCrossTreeConstraint(new Imply("process-url.json", "Image url"));
		faceRect.addCrossTreeConstraint(new Imply("process-file.json", "Content"));
		
		faceRect.addCrossTreeConstraint(new AssignedValue("FaceRect API", true));
		return faceRect;
	}
}

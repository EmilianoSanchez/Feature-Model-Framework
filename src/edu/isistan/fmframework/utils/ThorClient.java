package edu.isistan.fmframework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.evaluation.SXFMtoFM;
import fm.FeatureModelException;

public class ThorClient {

	public static final String SPLOT_MODELS_DIR = "C:\\Users\\emili\\Google Drive\\WorkspaceGitHub\\Feature-Model-Framework\\Models\\SPLOT-models";
	public static String OUTPUT_FOLDER_THOR;

	public static final String THORCOM_ABSOLUTE_PATH = "C:\\thor-avm-master\\Thor\\ThorCOM\\bin\\Debug\\ThorCOM.exe";
	public static final String THORCOM_INPUTFILE_ABSOLUTE_PATH = "C:\\thor-avm-master\\Thor\\ThorCOM\\bin\\Debug\\thor_cli_input.txt";

	public static void main(String[] args) throws FeatureModelException, IOException {

		File folder = new File(SPLOT_MODELS_DIR);
		File[] files = folder.listFiles();

		// Sample1
		OUTPUT_FOLDER_THOR = "C:\\Users\\emili\\Google Drive\\WorkspaceGitHub\\Feature-Model-Framework\\Models\\Thor-attributes-Sample1";
		
		System.out.println("Generating attributes for Sample #1");
		for (int i = 0; i < files.length; i++) {
			System.out.println("Executing " + (i + 1) + " of " + files.length);
			File modelfile = files[i];
			System.out.println(modelfile.getAbsolutePath());

			FeatureModel model = SXFMtoFM.parse(modelfile.getPath(), true);

			ThorParameters parameters = new ThorParameters();
			parameters.model_file_path = modelfile.getAbsolutePath();
			parameters.interaction_count = model.getNumFeatures();
			parameters.interaction_degrees = "100";
			parameters.writeIntoFile(THORCOM_INPUTFILE_ABSOLUTE_PATH);

			if (executeThorCOM()) {
				renameOutputFiles(modelfile);
			}
		}

		// Sample2
		OUTPUT_FOLDER_THOR = "C:\\Users\\emili\\Google Drive\\WorkspaceGitHub\\Feature-Model-Framework\\Models\\Thor-attributes-Sample2";
		
		System.out.println("Generating attributes for Sample #2");
		for (int i = 0; i < files.length; i++) {
			System.out.println("Executing " + (i + 1) + " of " + files.length);
			File modelfile = files[i];
			System.out.println(modelfile.getAbsolutePath());

			FeatureModel model = SXFMtoFM.parse(modelfile.getPath(), true);

			ThorParameters parameters = new ThorParameters();
			parameters.model_file_path = modelfile.getAbsolutePath();
			parameters.interaction_count = model.getNumFeatures() * 2;
			parameters.interaction_degrees = "67 23 10";
			parameters.writeIntoFile(THORCOM_INPUTFILE_ABSOLUTE_PATH);

			if (executeThorCOM()) {
				renameOutputFiles(modelfile);
			}
		}

	}

	private static void renameOutputFiles(File file) {
		File fileFeatureValues = new File(OUTPUT_FOLDER_THOR + "\\featSolution.txt");
		File newFileFeatureValues = new File(OUTPUT_FOLDER_THOR + "\\" + file.getName() + "-featSolution.txt");
		if (fileFeatureValues.exists()) {
			fileFeatureValues.renameTo(newFileFeatureValues);
		} else {
			System.err.println(fileFeatureValues.getName() + " does not exist");
		}

		File fileInteractionValues = new File(OUTPUT_FOLDER_THOR + "\\interactionSolution.txt");
		File newFileInteractionValues = new File(
				OUTPUT_FOLDER_THOR + "\\" + file.getName() + "-interactionSolution.txt");
		if (fileInteractionValues.exists()) {
			fileInteractionValues.renameTo(newFileInteractionValues);
		} else {
			System.err.println(fileInteractionValues.getName() + " does not exist");
		}

		File fileConfigurationValues = new File(OUTPUT_FOLDER_THOR + "\\variantSolution.txt");
		File newFileConfigurationValues = new File(OUTPUT_FOLDER_THOR + "\\" + file.getName() + "-variantSolution.txt");
		if (fileConfigurationValues.exists()) {
			fileConfigurationValues.renameTo(newFileConfigurationValues);
		} else {
			System.err.println(fileConfigurationValues.getName() + " does not exist");
		}
	}

	private static boolean executeThorCOM() throws IOException {

		boolean executedCorrect = false;
		ProcessBuilder builder = new ProcessBuilder(THORCOM_ABSOLUTE_PATH, THORCOM_INPUTFILE_ABSOLUTE_PATH);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = r.readLine()) != null) {
			System.out.println(line);
			if (line.equals("Done.")) {
				executedCorrect = true;
				break;
			}
		}

		p.destroy();
		try {
			int exitValue = p.waitFor();
			System.out.println("\nOutput code: " + exitValue);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		return executedCorrect;
	}

	private static class ThorParameters {

		static final String INPUT_ATTRIBUTE_VALUES_FOLDER = "C:\\thor-avm-master\\Thor\\InteracGenerator\\FeatureValues";

		public String output_folder_thor = OUTPUT_FOLDER_THOR;

		public String log_folder = OUTPUT_FOLDER_THOR;
		public boolean logging = false;

		public String model_file_path = "C:\\Users\\emili\\Google Drive\\WorkspaceGitHub\\Feature-Model-Framework\\Models\\SPLOT-models\\model-aircraft_fm.xml";

		// Performance AJStats: WORKING
		String feature_distribution_absolute_path = INPUT_ATTRIBUTE_VALUES_FOLDER + "\\performance\\AJStats_fv.txt";
		String interaction_distribution_absolute_path = INPUT_ATTRIBUTE_VALUES_FOLDER + "\\performance\\AJStats_iv.txt";
		String variant_distribution_absolute_path = INPUT_ATTRIBUTE_VALUES_FOLDER
				+ "\\performance\\variants\\AJStats_allMeasurements.xml_variants.txt";
		// Performance LLVM: NOT WORKING
		// String feature_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\LLVM_fv.txt";
		// String interaction_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\LLVM_iv.txt";
		// String variant_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\variants\\LLVM_Allmeasurements.xml_variants.txt";
		// Performance x264: NOT WORKING
		// String feature_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\x264_fv.txt";
		// String interaction_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\x264_iv.txt";
		// String variant_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\variants\\h264_measurements.xml_variants.txt";
		// Performance Email: NOT WORKING
		// String feature_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\Email_fv.txt";
		// String interaction_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\Email_iv.txt";
		// String variant_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\performance\\variants\\Email_allMeasurements.xml_variants.txt";
		// MainMemory SQLite: NOT WORKING
		// String feature_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\mainmemory\\SQLite_fv.txt";
		// String interaction_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\mainmemory\\SQLite_iv.txt";
		// String variant_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\mainmemory\\variants\\sqlite_measurements.xml_variants.txt";
		// MainMemory LLVM: NOT WORKING
		// String feature_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\mainmemory\\LLVM_fv.txt";
		// String interaction_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\mainmemory\\LLVM_iv.txt";
		// String variant_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\mainmemory\\variants\\llvm_measurements.xml_variants.txt";
		// BinarySize Violet: WORKING
		// String feature_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\binarySize\\Violet_fv.txt";
		// String interaction_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\binarySize\\Violet_iv.txt";
		// String variant_distribution_absolute_path =
		// INPUT_ATTRIBUTE_VALUES_FOLDER+"\\binarySize\\variants\\violetCorrect_measurementsAllConfigs.xml_variants.txt";

		public int interaction_count = 2;
		public String interaction_degrees = "80 20";
		public boolean feature_fitness = true;
		public boolean interaction_fitness = true;
		public boolean variant_fitness = false;
		public boolean variant = false;

		public boolean pseudo_random = true;
		public int number_config_size = 50;
		public int solver_timeout_seconds = 3;

		public boolean parallel_nsga = false;
		public int max_evaluation = 500;
		public int population_size = 50;
		public boolean chi_squared = true;

		public int weight_feature = 50;
		public int weight_interaction = 50;
		public int weight_variant = 0;

		public ThorParameters() {
		}

		public void writeIntoFile(String filePath) throws FileNotFoundException {
			File fnew = new File(filePath);
			PrintStream f = new PrintStream(fnew);

			f.println("set output " + this.output_folder_thor);
			f.println();
			f.println("set log_folder " + this.log_folder);
			f.println("set logging " + this.logging);
			f.println();
			f.println("load model " + this.model_file_path);
			f.println("load feature_distribution " + this.feature_distribution_absolute_path);
			f.println("load interaction_distribution " + this.interaction_distribution_absolute_path);
			f.println("load variant_distribution " + this.variant_distribution_absolute_path);
			f.println();
			f.println("set interaction_count " + this.interaction_count);
			f.println("set interaction_degrees " + this.interaction_degrees);
			f.println("set feature_fitness " + this.feature_fitness);
			f.println("set interaction_fitness " + this.interaction_fitness);
			f.println("set variant_fitness " + this.variant_fitness);
			f.println("set variant " + this.variant);
			f.println();
			f.println("set pseudo_random " + this.pseudo_random);
			f.println("set number_config_size " + this.number_config_size);
			f.println("set solver_timeout_seconds " + this.solver_timeout_seconds);
			f.println();
			f.println("set parallel_nsga " + this.parallel_nsga);
			f.println("set max_evaluation " + this.max_evaluation);
			f.println("set population_size " + this.population_size);
			f.println("set chi_squared " + this.chi_squared);
			f.println();
			f.println("set weight_feature " + this.weight_feature);
			f.println("set weight_interaction " + this.weight_interaction);
			f.println("set weight_variant " + this.weight_variant);

			f.close();
		}
	}
}

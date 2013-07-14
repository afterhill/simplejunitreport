package simple.junit.reporter;

import static simple.junit.utils.Strings.concat;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.taskdefs.XSLTProcess.Param;
import org.apache.tools.ant.taskdefs.optional.junit.AggregateTransformer;
import org.apache.tools.ant.taskdefs.optional.junit.XMLResultAggregator;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.URLResource;
import org.apache.tools.ant.util.FileUtils;

public class ReportTransformer extends AggregateTransformer {

	private static final String XSL_FILE_PATH = "";
	private Path classpath;

	private final List<Param> params;

	private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

	private static int counter;

	public ReportTransformer(Task task) {
		super(task);
		params = new CopyOnWriteArrayList<Param>();
	}

	@Override
	public Param createParam() {
		Param p = new Param();
		params.add(p);
		return p;
	}

	@Override
	public void transform() throws BuildException {
		checkOptions();
		TempFile tempFileTask = tempFileTask();
		XSLTProcess xsltTask = xsltTask();
		File outputFile = outputFile(tempFileTask);
		xsltTask.setOut(outputFile);
		createNewParams(xsltTask);
		createOutputDirParam(xsltTask);
		long startingTime = System.currentTimeMillis();
		try {
			xsltTask.execute();
		} catch (Exception e) {
			throw new BuildException(concat(
					"Errors while applying transformations: ", e.getMessage()),
					e);
		}
		long transformTime = System.currentTimeMillis() - startingTime;
		task.log(concat("Transform time: ", String.valueOf(transformTime),
				" ms"));
		delete(outputFile);
	}

	private XSLTProcess xsltTask() {
		XSLTProcess xsltTask = new XSLTProcess();
		xsltTask.bindToOwner(task);
		xsltTask.setClasspath(classpath);
		xsltTask.setXslResource(getStylesheet());
		xsltTask.setIn(((XMLResultAggregator) task).getDestinationFile());
		return xsltTask;
	}

	@Override
	protected Resource getStylesheet() {
		String xslname = "junit-frames.xsl";
		if (NOFRAMES.equals(format))
			xslname = "junit-noframes.xsl";
		if (styleDir == null) {
			URLResource stylesheet = new URLResource();
			URL stylesheetURL = getClass().getClassLoader().getResource(
					concat(XSL_FILE_PATH, xslname));
			stylesheet.setURL(stylesheetURL);
			return stylesheet;
		}
		FileResource stylesheet = new FileResource();
		File stylesheetFile = new File(styleDir, xslname);
		stylesheet.setFile(stylesheetFile);
		return stylesheet;
	}

	private TempFile tempFileTask() {
		TempFile tempFileTask = new TempFile();
		tempFileTask.bindToOwner(task);
		return tempFileTask;
	}

	private File outputFile(TempFile tempFileTask) {
		Project project = task.getProject();
		if (format.equals(FRAMES)) {
			String tempFileProperty = concat(getClass().getName(),
					String.valueOf(counter++));
			setUpTempFileTask(tempFileTask, tempFileProperty);
			return new File(project.getProperty(tempFileProperty));
		}
		return new File(toDir, "junit-noframes.html");
	}

	private void setUpTempFileTask(TempFile tempFileTask,
			String tempFileProperty) {
		Project project = task.getProject();
		File tmp = FILE_UTILS.resolveFile(project.getBaseDir(),
				project.getProperty("java.io.tmpdir"));
		tempFileTask.setDestDir(tmp);
		tempFileTask.setProperty(tempFileProperty);
		tempFileTask.execute();
	}

	private void createNewParams(XSLTProcess xsltTask) {
		for (Param param : params) {
			Param p = xsltTask.createParam();
			p.setProject(task.getProject());
			p.setName(param.getName());
			p.setExpression(param.getExpression());
		}
	}

	private void createOutputDirParam(XSLTProcess xsltTask) {
		Param p = xsltTask.createParam();
		p.setProject(task.getProject());
		p.setName("output.dir");
		p.setExpression(toDir.getAbsolutePath());
	}

	private void delete(File outputFile) {
		if (!format.equals(FRAMES))
			return;
		Delete deleteTask = new Delete();
		deleteTask.bindToOwner(task);
		deleteTask.setFile(outputFile);
		deleteTask.execute();
	}

	public void setClasspath(Path classpath) {
		createClasspath().append(classpath);
	}

	public void setClasspathRef(Reference r) {
		createClasspath().setRefid(r);
	}

	public Path createClasspath() {
		if (classpath == null)
			classpath = new Path(task.getProject());
		return classpath.createPath();
	}
}

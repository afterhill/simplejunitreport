package simple.junit.reporter;

import org.apache.tools.ant.taskdefs.optional.junit.AggregateTransformer;
import org.apache.tools.ant.taskdefs.optional.junit.XMLResultAggregator;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class JUnitReportTask extends XMLResultAggregator {

	private Path classpath;

	@SuppressWarnings("unchecked")
	@Override
	public AggregateTransformer createReport() {
		ReportTransformer transformer = new ReportTransformer(this);
		transformer.setClasspath(classpath);
		transformers.addElement(transformer);
		return transformer;
	}

	public void setClasspath(Path classpath) {
		createClasspath().append(classpath);
	}

	public void setClasspathRef(Reference r) {
		createClasspath().setRefid(r);
	}

	public Path createClasspath() {
		if (classpath == null)
			classpath = new Path(getProject());
		return classpath.createPath();
	}
}

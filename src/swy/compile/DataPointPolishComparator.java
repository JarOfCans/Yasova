package swy.compile;

import java.util.Comparator;

import swy.core.RaceTime;

public class DataPointPolishComparator implements Comparator<DataPoint> {

	@Override
	public int compare(DataPoint arg0, DataPoint arg1) {
		int arg0count = Math.min(arg0.getDataCount(), DataRead.polishMax);
		int arg1count = Math.min(arg1.getDataCount(), DataRead.polishMax);
		//System.out.printf("%s %s + %s-%s %s + %s: %d-%d | %.4f-%.4f = %.4f%s",arg0.course, arg0.character1, arg0.character2,arg1.course, arg1.character1, arg1.character2,arg0.getDataCount(), arg1.getDataCount(), arg0.averagePercentile(),arg1.averagePercentile(), ((arg0.getDataCount() == arg0.getDataCount())? arg1.averagePercentile()*10000 - arg0.averagePercentile()*10000:arg0.getDataCount() - arg1.getDataCount()), System.lineSeparator());
		return (arg0.getCourseId() == arg1.getCourseId())?
				(int) ((arg0count == arg1count)? arg1.averagePercentile()*100000 - arg0.averagePercentile()*100000:arg1count - arg0count):
					arg0.getCourseId() - arg1.getCourseId();
	}

}

import java.util.*;

public class FrameContainer {
	
	ArrayList<FrameSender> frames = new ArrayList<FrameSender>();
	
	public void add(FrameSender fs) {
		frames.add(fs);
	}
	public void remove(FrameSender fs) {
		frames.remove(fs);
	}
	
	public void sendFrames(Tavolo tav) {
		for(FrameSender fs : frames) {
			fs.sendInfo(tav);
		}
	}
}

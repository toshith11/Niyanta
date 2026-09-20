package commander.memory;

import commander.core.Knowledge;

import java.util.ArrayList;
import java.util.List;

public class WorkingMemory {

    private final List<Knowledge> memories;

    public WorkingMemory() {
        memories = new ArrayList<>();
    }

    public void add(Knowledge knowledge) {

        if (knowledge != null) {
            memories.add(knowledge);
        }
    }

    public List<Knowledge> getAll() {
        return new ArrayList<>(memories);
    }

    public void clear() {
        memories.clear();
    }
}
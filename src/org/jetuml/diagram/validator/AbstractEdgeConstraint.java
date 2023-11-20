package org.jetuml.diagram.validator;

import org.jetuml.diagram.Diagram;
import org.jetuml.diagram.Edge;
import org.jetuml.gui.EditorFrame;
import org.jetuml.gui.ToastNotification;

/**
 * Implementation of the general scaffolding for creating an Edge Constraint.
 * Uses the Template design pattern.
 */
public abstract class AbstractEdgeConstraint implements EdgeConstraint
{

    private static EditorFrame aEditorFrame;

    public static void setEditorFrame(EditorFrame pEditorFrame)
    {
        aEditorFrame = pEditorFrame;
    }

    /**
     * Determines if a constraint is satisfied, and spawns a notification if not.
     *
     * @param pEdge The edge being validated.
     * @param pDiagram The diagram containing the edge.
     * @return True if the edge is satisfied.
     * @pre pEdge != null && pDiagram != null && pDiagram.contains(pEdge)
     * @pre pEdge.start() != null && pEdge.end() != null;
     */
    public final boolean satisfied(Edge pEdge, Diagram pDiagram)
    {
        boolean result = check(pEdge, pDiagram);
        if (!result && aEditorFrame != null)
        {
            /* SPAWN NOTIFICATION */
            aEditorFrame.spawnNotification(description(), ToastNotification.Type.ERROR);
        }
        return result;
    }

    /**
     * Actual implementation of the constraint.
     *
     * @param pEdge The edge being validated.
     * @param pDiagram The diagram containing the edge.
     * @return True if the edge is satisfied.
     * @pre pEdge != null && pDiagram != null && pDiagram.contains(pEdge)
     * @pre pEdge.start() != null && pEdge.end() != null;
     */
    protected abstract boolean check(Edge pEdge, Diagram pDiagram);

    /**
     * @return A string to be displayed to the user when the constraint is not satisfied
     */
    protected abstract String description();

}

/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2023 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/
package org.jetuml.diagram.validator;

import java.util.Set;

import org.jetuml.diagram.Diagram;
import org.jetuml.diagram.DiagramType;
import org.jetuml.diagram.Edge;
import org.jetuml.diagram.Node;
import org.jetuml.diagram.edges.NoteEdge;
import org.jetuml.diagram.edges.UseCaseAssociationEdge;
import org.jetuml.diagram.edges.UseCaseDependencyEdge;
import org.jetuml.diagram.edges.UseCaseGeneralizationEdge;
import org.jetuml.diagram.nodes.ActorNode;
import org.jetuml.diagram.nodes.NoteNode;
import org.jetuml.diagram.nodes.UseCaseNode;

/**
 * Validator for use case diagrams.
 */
public class UseCaseDiagramValidator extends AbstractDiagramValidator
{
	private static final Set<AbstractEdgeConstraint> CONSTRAINTS = Set.of(
			new AbstractDiagramValidator.ConstraintMaxNumberOfEdgesOfGivenTypeBetweenNodes(1),
			new AbstractDiagramValidator.ConstraintNoSelfEdgeForEdgeType(UseCaseAssociationEdge.class),
			new AbstractDiagramValidator.ConstraintNoSelfEdgeForEdgeType(UseCaseGeneralizationEdge.class),
			new AbstractDiagramValidator.ConstraintNoSelfEdgeForEdgeType(UseCaseDependencyEdge.class),
			new UseCaseDiagramValidator.ConstraintNoEdgeConnectedToNote());

	private static final Set<Class<? extends Node>> VALID_NODE_TYPES = Set.of(
			ActorNode.class, 
			UseCaseNode.class);

	private static final Set<Class<? extends Edge>> VALID_EDGE_TYPES = Set.of(
			UseCaseAssociationEdge.class,
			UseCaseDependencyEdge.class, 
			UseCaseGeneralizationEdge.class);

	/**
	 * Creates a new validator for one use case diagram.
	 *
	 * @param pDiagram The diagram to do semantic validity check on.
	 * @pre pDiagram != null && pDiagram.getType() == DiagramType.USECASE
	 */

	public UseCaseDiagramValidator(Diagram pDiagram)
	{
		super(pDiagram, VALID_NODE_TYPES, VALID_EDGE_TYPES, CONSTRAINTS);
		assert pDiagram.getType() == DiagramType.USECASE;
	}

	private static final class ConstraintNoEdgeConnectedToNote extends AbstractEdgeConstraint
	{

		/**
		 * Actual implementation of the constraint.
		 *
		 * @param pEdge    The edge being validated.
		 * @param pDiagram The diagram containing the edge.
		 * @return True if the edge is satisfied.
		 * @pre pEdge != null && pDiagram != null && pDiagram.contains(pEdge)
		 * @pre pEdge.start() != null && pEdge.end() != null;
		 */
		@Override
		protected boolean check(Edge pEdge, Diagram pDiagram)
		{
			return !(pEdge.getClass() != NoteEdge.class &&
					(pEdge.start().getClass() == NoteNode.class ||
							pEdge.end().getClass() == NoteNode.class ));
		}

		/**
		 * @return A string to be displayed to the user when the constraint is not satisfied
		 */
		@Override
		protected String description()
		{
			return "NoEdgeConnectedToNote";
		}
	}
	
	/*
     * Only associate edges can connect to actors
	 */
	private static boolean constraintNoEdgeConnectedToNote(Edge pEdge, Diagram pDiagram)
	{
		return !(pEdge.getClass() != NoteEdge.class && 
				(pEdge.start().getClass() == NoteNode.class || 
				 pEdge.end().getClass() == NoteNode.class ));
	}
}

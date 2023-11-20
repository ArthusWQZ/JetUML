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
import org.jetuml.diagram.edges.AggregationEdge;
import org.jetuml.diagram.edges.AssociationEdge;
import org.jetuml.diagram.edges.DependencyEdge;
import org.jetuml.diagram.edges.GeneralizationEdge;
import org.jetuml.diagram.nodes.ClassNode;
import org.jetuml.diagram.nodes.InterfaceNode;
import org.jetuml.diagram.nodes.PackageDescriptionNode;
import org.jetuml.diagram.nodes.PackageNode;

/**
 * Validator for class diagrams.
 */
public class ClassDiagramValidator extends AbstractDiagramValidator
{

	private static final Set<AbstractEdgeConstraint> CONSTRAINTS = Set.of(
			new AbstractDiagramValidator.ConstraintMaxNumberOfEdgesOfGivenTypeBetweenNodes(1),
			new AbstractDiagramValidator.ConstraintNoSelfEdgeForEdgeType(GeneralizationEdge.class),
			new AbstractDiagramValidator.ConstraintNoSelfEdgeForEdgeType(DependencyEdge.class),
			new AbstractDiagramValidator.ConstraintNoDirectCyclesForEdgeType(DependencyEdge.class),
			new AbstractDiagramValidator.ConstraintNoDirectCyclesForEdgeType(GeneralizationEdge.class),
			new AbstractDiagramValidator.ConstraintNoDirectCyclesForEdgeType(AggregationEdge.class),
			new AbstractDiagramValidator.ConstraintNoDirectCyclesForEdgeType(AssociationEdge.class),
			new ClassDiagramValidator.ConstraintNoCombinedAssociationAggregation());

	private static final Set<Class<? extends Node>> VALID_NODE_TYPES = Set.of(
			ClassNode.class, 
			InterfaceNode.class,
			PackageNode.class, 
			PackageDescriptionNode.class);

	private static final Set<Class<? extends Edge>> VALID_EDGE_TYPES = Set.of(
			DependencyEdge.class,
			GeneralizationEdge.class, 
			AssociationEdge.class, 
			AggregationEdge.class);

	/**
	 * Creates a new validator for a class diagram.
	 *
	 * @param pDiagram The diagram to validate
	 * @pre pDiagram != null && pDiagram.getType() == DiagramType.CLASS
	 */
	public ClassDiagramValidator(Diagram pDiagram)
	{
		super(pDiagram, VALID_NODE_TYPES, VALID_EDGE_TYPES, CONSTRAINTS);
		assert pDiagram.getType() == DiagramType.CLASS;
	}

	private static final class ConstraintNoCombinedAssociationAggregation extends AbstractEdgeConstraint
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
			return pDiagram.edges().stream()
					.filter(ClassDiagramValidator::isAssociationOrAggregation)
					.filter(edge -> isBetweenSameNodes(edge, pEdge))
					.count() <= 1;
		}

		/**
		 * @return A string to be displayed to the user when the constraint is not satisfied
		 */
		@Override
		protected String description()
		{
			return "NoCombinedAssociationAggregation";
		}
	}
	
	/**
	 * There can't be both an association and an aggregation edge between two
	 * nodes.
	 */
	public static boolean constraintNoCombinedAssociationAggregation(Edge pEdge, Diagram pDiagram)
	{
		return pDiagram.edges().stream()
				.filter(ClassDiagramValidator::isAssociationOrAggregation)
				.filter(edge -> isBetweenSameNodes(edge, pEdge))
				.count() <= 1;
	}
	
	/*
	 * Aggregation edges and association edges are in the same category
	 */
	private static boolean isAssociationOrAggregation(Edge pEdge)
	{
		return pEdge.getClass() == AssociationEdge.class || pEdge.getClass() == AggregationEdge.class;
	}
	
	/*
	 * Irrespective of direction
	 */
	private static boolean isBetweenSameNodes(Edge pEdge1, Edge pEdge2)
	{
		return pEdge1.start() == pEdge2.start() && pEdge1.end() == pEdge2.end() ||
				pEdge1.start() == pEdge2.end() && pEdge1.end() == pEdge2.start();
	}
}

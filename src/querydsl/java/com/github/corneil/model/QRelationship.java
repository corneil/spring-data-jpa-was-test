package com.github.corneil.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRelationship is a Querydsl query type for Relationship
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRelationship extends EntityPathBase<Relationship> {

    private static final long serialVersionUID = -895669815L;

    public static final QRelationship relationship = new QRelationship("relationship");

    public final DateTimePath<java.util.Date> acceptedDate = createDateTime("acceptedDate", java.util.Date.class);

    public final BooleanPath archived = createBoolean("archived");

    public final DateTimePath<java.util.Date> archivedDate = createDateTime("archivedDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> createdDate = createDateTime("createdDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath reminderSent = createBoolean("reminderSent");

    public final EnumPath<RelationshipStatus> status = createEnum("status", RelationshipStatus.class);

    public final DateTimePath<java.util.Date> submittedDate = createDateTime("submittedDate", java.util.Date.class);

    public QRelationship(String variable) {
        super(Relationship.class, forVariable(variable));
    }

    public QRelationship(Path<? extends Relationship> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRelationship(PathMetadata<?> metadata) {
        super(Relationship.class, metadata);
    }

}


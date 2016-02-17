package org.granite.math;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class VectorTools {


    public static double dotProduct(final double[] vector1, final double[] vector2) {
        checkNotNull(vector1, "vector1");
        checkNotNull(vector2, "vector2");
        checkArgument(vector1.length == vector2.length, "dotProduct requires vectors of similar length");
        checkArgument(vector1.length > 0, "cannot find dotProduct of empty vector");

        double dotProduct = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
        }

        return dotProduct;
    }

    public static double norm(final double[] vector){
        return Math.sqrt(dotProduct(vector, vector));
    }

    public static double cosine(final double[] vector1, final double[] vector2){
        return dotProduct(vector1, vector2) / (norm(vector1) * norm(vector2));
    }

    public static double angularSimilarity(final double[] vector1, final double[] vector2){
        return 1.0 - (Math.acos(cosine(vector1, vector2)) / Math.PI);
    }

    public static double[] distanceVector(final double[] vector1, final double[] vector2) {
        checkNotNull(vector1, "vector1");
        checkNotNull(vector2, "vector2");
        checkArgument(vector1.length == vector2.length, "distance vector requires vectors of similar length");
        checkArgument(vector1.length > 0, "cannot find distance vector of empty vectors");

        double[] distance = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++) {
            distance[i] = vector1[i] - vector2[i];
        }

        return distance;
    }

    public static double euclideanDistance(final double[] vector1, final double[] vector2){
        return norm(distanceVector(vector1, vector2));
    }


}

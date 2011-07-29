/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.geom.codec;

import org.geolatte.geom.GeometryType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Karel Maesen, Geovise BVBA, 2011
 */
public class TestWKTTokenizer {
    private WKTWordMatcher words = new PGWKTWordMatcher15();


    @Test
    public void test_only_whitespace() {
        String wkt = "    ";
        WKTTokenizer tokenizer = new WKTTokenizer(wkt, words);
        assertFalse(tokenizer.moreTokens());
    }


    @Test
    public void test_tokenize_empty_point() {
        String wkt = "POINT EMPTY";
        WKTTokenizer tokens = new WKTTokenizer(wkt, words);
        assertTrue(tokens.moreTokens());
        WKTToken.Geometry token = (WKTToken.Geometry) (tokens.nextToken());
        assertEquals(GeometryType.POINT, token.getType());
        assertFalse(token.isMeasured());
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.Empty);
    }


    @Test
    public void test_tokenize_point() {
        String wkt = "POINT (20 33.3)";
        WKTTokenizer tokens = new WKTTokenizer(wkt, words);
        assertTrue(tokens.moreTokens());
        WKTToken.Geometry token = (WKTToken.Geometry) (tokens.nextToken());
        assertEquals(GeometryType.POINT, token.getType());
        assertFalse(token.isMeasured());
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.moreTokens());
        WKTToken.PointSequence pstoken = (WKTToken.PointSequence) (tokens.nextToken());
        assertEquals(1, pstoken.getPoints().size());
        assertEquals(20, pstoken.getPoints().getX(0), Math.ulp(20d));
        assertEquals(33.3, pstoken.getPoints().getY(0), Math.ulp(20d));
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertFalse(tokens.moreTokens());
    }

    @Test
    public void test_tokenize_point_3D() {
        String wkt = "POINT (20 33.3 .24)";
        WKTTokenizer tokens = new WKTTokenizer(wkt, words);
        assertTrue(tokens.moreTokens());
        WKTToken.Geometry token = (WKTToken.Geometry) (tokens.nextToken());
        assertEquals(GeometryType.POINT, token.getType());
        assertFalse(token.isMeasured());
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.moreTokens());
        WKTToken.PointSequence pstoken = (WKTToken.PointSequence) (tokens.nextToken());
        assertEquals(1, pstoken.getPoints().size());
        assertEquals(20, pstoken.getPoints().getX(0), Math.ulp(20d));
        assertEquals(33.3, pstoken.getPoints().getY(0), Math.ulp(20d));
        assertEquals(0.24, pstoken.getPoints().getZ(0), Math.ulp(2d));
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertFalse(tokens.moreTokens());
    }

    @Test
    public void test_tokenize_point_M() {
        String wkt = "POINTM (20 33.3 .24)";
        WKTTokenizer tokens = new WKTTokenizer(wkt, words);
        assertTrue(tokens.moreTokens());
        WKTToken.Geometry token = (WKTToken.Geometry) (tokens.nextToken());
        assertEquals(GeometryType.POINT, token.getType());
        assertTrue(token.isMeasured());
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.moreTokens());
        WKTToken.PointSequence pstoken = (WKTToken.PointSequence) (tokens.nextToken());
        assertEquals(1, pstoken.getPoints().size());
        assertEquals(20, pstoken.getPoints().getX(0), Math.ulp(20d));
        assertEquals(33.3, pstoken.getPoints().getY(0), Math.ulp(20d));
        assertEquals(0.24, pstoken.getPoints().getM(0), Math.ulp(2d));
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertFalse(tokens.moreTokens());
    }

    @Test
    public void test_tokenize_linestring() {
        String wkt = "LINESTRING(20 33.3 .24 , .1 2 3)";
        WKTTokenizer tokens = new WKTTokenizer(wkt, words);
        assertTrue(tokens.moreTokens());
        WKTToken.Geometry token = (WKTToken.Geometry) (tokens.nextToken());
        assertEquals(GeometryType.LINE_STRING, token.getType());
        assertFalse(token.isMeasured());
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.moreTokens());
        WKTToken.PointSequence pstoken = (WKTToken.PointSequence) (tokens.nextToken());
        assertEquals(2, pstoken.getPoints().size());
        assertEquals(20, pstoken.getPoints().getX(0), Math.ulp(20d));
        assertEquals(33.3, pstoken.getPoints().getY(0), Math.ulp(20d));
        assertEquals(0.24, pstoken.getPoints().getZ(0), Math.ulp(2d));
        assertEquals(.1d, pstoken.getPoints().getX(1), Math.ulp(20d));
        assertEquals(2d, pstoken.getPoints().getY(1), Math.ulp(20d));
        assertEquals(3d, pstoken.getPoints().getZ(1), Math.ulp(2d));
        assertTrue(tokens.moreTokens());
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertFalse(tokens.moreTokens());
    }

    @Test
    public void test_tokenize_polygon() {
        String wkt = "POLYGON((5 5, 1 0, 1 1 ,0 1, 3 3),(0.25 0.25, 0.25 0.5, 0.5 0.5, 0.5 0.25, 0.25 0.25))";
        WKTTokenizer tokens = new WKTTokenizer(wkt, words);
        assertTrue(tokens.moreTokens());
        WKTToken.Geometry token = (WKTToken.Geometry) (tokens.nextToken());
        assertEquals(GeometryType.POLYGON, token.getType());
        assertFalse(token.isMeasured());
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.nextToken() instanceof WKTToken.PointSequence);
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertTrue(tokens.nextToken() instanceof WKTToken.ElementSeparator);
        assertTrue(tokens.nextToken() instanceof WKTToken.StartList);
        assertTrue(tokens.nextToken() instanceof WKTToken.PointSequence);
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertTrue(tokens.nextToken() instanceof WKTToken.EndList);
        assertFalse(tokens.moreTokens());
    }


}



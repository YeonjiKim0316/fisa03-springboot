package com.example.demo;

import org.junit.jupiter.api.*;

public class TestLifeCycle {

    @BeforeAll // 모든 테스트 실행 전 딱 1번
    static void beforeAll() {
        System.out.println("## BeforeAll Annotation 호출 ##");
        System.out.println();
    }

    @AfterAll // 모든 테스트 완료 후 딱 1번
    static void afterAll() {
        System.out.println("## AfterAll Annotation 호출 ##");
        System.out.println();
    }

    @BeforeEach  // @Test로 분리해놓은 각 유닛 테스트 실행 전에 실행
    void beforeEach() {
        System.out.println("## BeforeEach Annotation 호출 ##");
        System.out.println();
    }

    @AfterEach // @Test로 분리해놓은 각 유닛 테스트 실행 후에 실행
    void afterEach() {
        System.out.println("## AfterEach Annotation 호출 ##");
        System.out.println();
    }

    @Test
    void test1() {
        System.out.println("## test1 시작 ##");
        System.out.println();
    }

    @Test
    @DisplayName("Test Case 2!!!")
    void test2() {
        System.out.println("## test2 시작 ##");
        System.out.println();
    }

    @Test
    @Disabled
    void test3() {
        System.out.println("## test3 시작 ##");
        System.out.println();
    }

}
package com.example.rideshare.payment.strategy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentStrategyFactory {

    private final List<PaymentStrategy> strategies;

    // Spring automatically injects ALL classes that implement PaymentStrategy
    public PaymentStrategyFactory(List<PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public PaymentStrategy getStrategy(String paymentMethod) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(paymentMethod))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment method not supported: " + paymentMethod));
    }
}
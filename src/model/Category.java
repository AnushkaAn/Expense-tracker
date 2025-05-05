// Category.java
package model;

public enum Category {
    FOOD("🍕"), RENT("🏠"), ENTERTAINMENT("🎬"), TRANSPORTATION("🚗"), 
    UTILITIES("💡"), HEALTHCARE("🏥"), EDUCATION("🎓"), SHOPPING("🛍️"), 
    TRAVEL("✈️"), GIFTS("🎁"), SUBSCRIPTIONS("📱"), SALARY("💰"), 
    INVESTMENTS("📈"), SAVINGS("💳"), DINING("🍽️"), GROCERIES("🛒"), 
    CLOTHING("👕"), FITNESS("💪"), PETS("🐶"), CHARITY("❤️"), 
    HOBBY("🎨"), ELECTRONICS("💻"), OTHER("📌");
    
    private final String emoji;
    
    Category(String emoji) {
        this.emoji = emoji;
    }
    
    @Override
    public String toString() {
        return emoji + " " + name().charAt(0) + name().substring(1).toLowerCase();
    }
}
class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
}

abstract class Room {
    private int number;
    private int capacity;
    private int nightlyRate;
    private boolean reserved;
    
    public Room(int number, int capacity, int nightlyRate) {
        this.number = number;
        this.capacity = capacity;
        this.nightlyRate = nightlyRate;
        this.reserved = false;
    }
    
    public Room(int number, int nightlyRate) {
        this(number, (int)(Math.random() * 4) + 2, nightlyRate);
    }
    
    public int getNumber() { return number; }
    public int getCapacity() { return capacity; }
    public int getNightlyRate() { return nightlyRate; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
    
    public String getInfo() {
        return "№" + number + ", вмещает: " + capacity + ", цена: " + nightlyRate + ", статус: " + (reserved ? "забронирован" : "свободен");
    }
}

abstract class ProRoom extends Room {
    public ProRoom(int number, int capacity, int nightlyRate) {
        super(number, capacity, nightlyRate);
    }
    
    public ProRoom(int number, int nightlyRate) {
        super(number, nightlyRate);
    }
}

class StandardRoom extends Room {
    public StandardRoom(int number, int capacity, int nightlyRate) {
        super(number, capacity, nightlyRate);
    }
    
    public StandardRoom(int number, int nightlyRate) {
        super(number, nightlyRate);
    }
}

class LuxuryRoom extends ProRoom {
    public LuxuryRoom(int number, int capacity, int nightlyRate) {
        super(number, capacity, nightlyRate);
    }
    
    public LuxuryRoom(int number, int nightlyRate) {
        super(number, nightlyRate);
    }
}

class BusinessRoom extends ProRoom {
    public BusinessRoom(int number, int capacity, int nightlyRate) {
        super(number, capacity, nightlyRate);
    }
    
    public BusinessRoom(int number, int nightlyRate) {
        super(number, nightlyRate);
    }
}

interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void release(T room);
}

class HotelService<T extends Room> implements RoomService<T> {
    
    @Override
    public void clean(T room) {
        System.out.println("Уборка комнаты №" + room.getNumber() + " завершена");
    }
    
    @Override
    public void reserve(T room) {
        if (room.isReserved()) {
            throw new BookingException("Комната №" + room.getNumber() + " уже занята");
        }
        room.setReserved(true);
        System.out.println("Комната №" + room.getNumber() + " успешно забронирована");
    }
    
    @Override
    public void release(T room) {
        room.setReserved(false);
        System.out.println("Комната №" + room.getNumber() + " освобождена");
    }
}

public class HotelManagement {
    public static void main(String[] args) {
        HotelService<Room> service = new HotelService<>();
        
        StandardRoom standard = new StandardRoom(101, 2000);
        LuxuryRoom luxury = new LuxuryRoom(501, 8000);
        BusinessRoom business = new BusinessRoom(301, 4500);
        
        demonstrateRoom(service, standard);
        demonstrateRoom(service, luxury);
        demonstrateRoom(service, business);
        
        testDoubleBooking(service, standard);
    }
    
    private static void demonstrateRoom(HotelService<Room> service, Room room) {
        System.out.println("\n--- Работа с комнатой ---");
        System.out.println(room.getInfo());
        
        service.clean(room);
        service.reserve(room);
        System.out.println("После брони: " + room.isReserved());
        
        service.release(room);
        System.out.println("После освобождения: " + room.isReserved());
    }
    
    private static void testDoubleBooking(HotelService<Room> service, Room room) {
        System.out.println("\n--- Проверка повторного бронирования ---");
        service.reserve(room);
        
        try {
            service.reserve(room);
        } catch (BookingException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        
        service.release(room);
    }
}
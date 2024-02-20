package com.cinntra.ledure.calender;

public interface HorizontalPickerListener {
    void onStopDraggingPicker();
    void onDraggingPicker();
    void onDateSelected(Day item);
    void onTodayClick(Day item);
}

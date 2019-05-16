package itis.ru.kpfu.join.data

sealed class EventType

class ProjectAddedEvent: EventType()

class LeaveFromProjectEvent: EventType()

class EditProjectEvent: EventType()
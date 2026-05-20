INSERT INTO lesson (id, title, concept, difficulty, module_number, estimated_minutes) VALUES
(1, 'Classes & Objects', 'OOP - Classes & Objects', 'beginner', 1, 5),
(2, 'Inheritance', 'OOP - Inheritance', 'beginner', 2, 5),
(3, 'Encapsulation', 'OOP - Encapsulation', 'intermediate', 3, 5);

INSERT INTO question (id, lesson_id, text, option_a, option_b, option_c, option_d, correct_option) VALUES
-- Lesson 1: Classes & Objects
(1,  1, 'What keyword is used to create an object in Java?',
     'define', 'new', 'create', 'object', 'B'),
(2,  1, 'Which keyword is used to define a class in Java?',
     'object', 'struct', 'class', 'type', 'C'),
(3,  1, 'What is the default value of an int field in a class?',
     'null', '1', '0', '-1', 'C'),
(4,  1, 'Which of the following is a valid class declaration in Java?',
     'Class MyClass {}', 'public class MyClass {}', 'class = MyClass {}', 'define MyClass {}', 'B'),
(5,  1, 'What is a constructor primarily used for?',
     'Destroy objects', 'Initialize objects', 'Copy objects', 'Compare objects', 'B'),

-- Lesson 2: Inheritance
(6,  2, 'Which keyword is used to inherit from a parent class in Java?',
     'implements', 'inherits', 'extends', 'super', 'C'),
(7,  2, 'What is the parent class of all Java classes?',
     'Base', 'Root', 'Main', 'Object', 'D'),
(8,  2, 'Which keyword is used to call a parent class constructor?',
     'parent()', 'super()', 'this()', 'base()', 'B'),
(9,  2, 'Can a subclass override a method from its parent class?',
     'No', 'Only private methods', 'Only with same parameters', 'Yes, with the same signature', 'D'),
(10, 2, 'What does method overriding mean?',
     'Defining a new method', 'Redefining a parent method in a subclass', 'Calling a method', 'Deleting a method', 'B'),

-- Lesson 3: Encapsulation
(11, 3, 'Which access modifier restricts access to within the same class only?',
     'public', 'protected', 'default', 'private', 'D'),
(12, 3, 'What is the purpose of a getter method?',
     'Set a field value', 'Delete a field', 'Return a field value', 'Compare field values', 'C'),
(13, 3, 'Which OOP principle is encapsulation most related to?',
     'Code reuse', 'Data hiding', 'Multiple inheritance', 'Polymorphism', 'B'),
(14, 3, 'Which keyword prevents a method from being overridden in Java?',
     'static', 'final', 'private', 'sealed', 'B'),
(15, 3, 'Which of the following best demonstrates good encapsulation?',
     'Public fields only', 'No getters or setters', 'Private fields with public getters/setters', 'Static methods only', 'C');

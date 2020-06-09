package org.fhict.fhictcompanion;

public class Person {
    private String Id;
    private String FirstName;
    private String LastName;
    private  String Initials;
    private  String Mail;
    private  String Office;
    private  String Phone;
    private  String Department;
    private  String Title;

    public Person() {
        this.Id = null;
        this.FirstName = null;
        this.LastName = null;
        this.Initials = null;
        this.Mail = null;
        this.Office = null;
        this.Phone = null;
        this.Department = null;
        this.Title = null;
    }

    public Person(String Id, String FirstName, String LastName, String Initials, String Mail, String Office, String Phone,
                  String Department, String Title)
    {
        this.Id = Id;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Initials = Initials;
        this.Mail = Mail;
        this.Office = Office;
        this.Phone = Phone;
        this.Department = Department;
        this.Title = Title;
    }
    public String getId()
    {
        return this.Id;
    }

    public String getFirstName()
    {
        return this.FirstName;
    }
    public String getLastName()
    {
        return this.LastName;
    }

    public String getName()
    {
        return (this.FirstName + " " + this.LastName);
    }

    public String getInitials() {
        return Initials;
    }

    public String getMail() {
        return Mail;
    }

    public String getOffice() {
        return Office;
    }

    public String getPhone() {
        return Phone;
    }

    public String getDepartment() {
        return Department;
    }

    public String getTitle() {
        return Title;
    }
}

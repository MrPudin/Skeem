package sstinc.skeem.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Locale;

import static java.lang.Math.abs;

//TODO: Locale datetime string format
//TODO: Check dependencies for datetime parcelable

/**
 * This class handles information relating to an instance in time using
 * Calendar. Milliseconds is the smallest denomination this class handles. The
 * class is parcelable.
 *
 * @see Calendar
 * @see Parcelable
 */
public class Datetime implements Parcelable {
    //Properties
    private static final DateTimeFormatter default_format = DateTimeFormat.forPattern(
            "yyyy-MM-dd HH:mm:ss");

    private org.joda.time.DateTime datetime;
    private boolean hasDate;
    private boolean hasTime;

    //Constructors
    /**
     * Default constructor.
     * Sets the calendar to the minimum date possible
     * for calendar to hold. {@link #hasDate} and {@link #hasTime} are false
     * to indicate absence of date and time.
     */
    public Datetime() {
        this.hasDate = false;
        this.hasTime = false;

        this.datetime = new org.joda.time.DateTime(0);
    }

    /**
     * Copy constructor.
     * Initialises new DateTime Object such that new Object is a Clone of
     * Datetime object passed to the method.
     *
     * @param datetime Object to clone/copy.
     */
    public Datetime(Datetime datetime) {
        this();

        if(datetime != null) {
            this.datetime = new org.joda.time.DateTime(datetime.getMillis());

            this.hasDate = datetime.getHasDate();
            this.hasTime = datetime.getHasTime();
        }
    }

    /**
     * Construct Datetime from org.joda.time.DateTime.
     * Initialises Datetime object from org.joda.time.DateTime.
     *
     * @param datetime datetime object to initialise form.
     */
    public Datetime(org.joda.time.DateTime datetime) {
        this();

        if(datetime != null) {
            this.hasDate = true;
            this.hasTime = true;

            this.datetime = datetime;
        }
    }

    /**
     * String constructor.
     * Creates the object by parsing the string passed.
     * Uses default empty constructor {@link #Datetime()} if string is empty.
     *
     * @param datetime The string of the datetime. Can be obtained by the
     *                 {@link #toString} method.
     */
    public Datetime(String datetime) {
        this();

        if (datetime != null && ! datetime.isEmpty()) {
            // Parse data from string
            String[] datetime_list = datetime.split(" ");
            String[] date_list = datetime_list[0].split("/");
            String[] time_list = datetime_list[1].split(":");

            // Check values
            // Date values
            if (Integer.parseInt(date_list[2]) != 0) {
                this.datetime = this.datetime.withYear(Integer.parseInt(date_list[0]));
                this.datetime = this.datetime.withMonthOfYear(Integer.parseInt(date_list[1]));
                this.datetime = this.datetime.withDayOfMonth(Integer.parseInt(date_list[2]));

                // Check that the date is not 1/1/1970
                this.hasDate = this.getDay() != 1 || this.getMonth() != 1 || this.getYear() != 1970;
            }

            // Time values
            if (!time_list[0].equals("--")) {
                this.datetime = this.datetime.withHourOfDay(Integer.parseInt(time_list[0]));
                this.datetime = this.datetime.withMinuteOfHour(Integer.parseInt(time_list[1]));

                this.hasTime = true;
            }
        }
    }

    // Object Operations

    /**
     * Static method to get the current datetime with the offset applied.
     * @return current phone local datetime with timezone offset applied
     */
    public static Datetime getCurrentDatetime() {
        return new Datetime(new DateTime(DateTimeZone.getDefault()).plus(
                Calendar.getInstance().getTimeZone().getRawOffset()));
    }

    /**
     * Compares only the dates of this instance of Datetime and another
     * Datetime.
     * @param datetime The Datetime instance to compare to
     * @return -1 if the current instance is before the compared instance
     *          0 if the current instance is equal to the compared instance
     *          1 if the current instance is after the compared instance
     */
    public int compareDates(Datetime datetime) {
        // Make a copy of the current instance and the compared instance
        Datetime current_instance = new Datetime(this);
        Datetime compared_instance = new Datetime(datetime);

        // Disable time
        current_instance.setHasTime(false);
        compared_instance.setHasTime(false);

        // Compare dates by milliseconds
        return Long.compare(current_instance.getMillis(), compared_instance.getMillis());
    }

    /**
     * Compares only the times of this instance of Datetime and another
     * Datetime.
     * @param datetime The Datetime instance to compare to
     * @return -1 if the current instance is before the compared instance
     *          0 if the current instance is equal to the compared instance
     *          1 if the current instance is after the compared instance
     */
    public int compareTimes(Datetime datetime) {
        // Make a copy of the current instance and the compared instance
        Datetime current_instance = new Datetime(this);
        Datetime compared_instance = new Datetime(datetime);

        // Disable time
        current_instance.setHasDate(false);
        compared_instance.setHasDate(false);

        // Compare dates by milliseconds
        return Long.compare(current_instance.getMillis(), compared_instance.getMillis());
    }

    @Override
    public boolean equals(Object object)
    {
        //Safety Check
        if(this == object) return true;
        if(object == null) return false;
        if(this.getClass() != object.getClass()) return false;

        //Property Check
        Datetime datetimeObject = (Datetime)object;
        if(this.getHasDate() == datetimeObject.getHasDate()
                && this.getHasTime() == datetimeObject.getHasDate()
                && this.getYear() == datetimeObject.getYear()
                && this.getMonth() == datetimeObject.getMonth()
                && this.getDay() == datetimeObject.getDay()
                && this.getHour() == datetimeObject.getHour()
                && this.getMinute() == datetimeObject.getMinute()
                && this.getMillis() == datetimeObject.getMillis())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Getters and Setters

    /**
     * Gets the datetime's year.
     * @return datetime's year
     */
    public int getYear() {
        return this.datetime.getYear();
    }

    /**
     * Gets the datetime's month.
     * @return datetime's month
     */
    public int getMonth() {
        return this.datetime.getMonthOfYear();
    }

    /**
     * Gets the datetime's day of the month.
     * @return datetime's day of month
     */
    public int getDay() {
        return this.datetime.getDayOfMonth();
    }

    /**
     * Gets the datetime's 24 hour value.
     * @return datetime's 24 hour value
     */
    public int getHour() {
        return this.datetime.getHourOfDay();
    }

    /**
     * Gets the datetime's minute
     * @return datetime's minute
     */
    public int getMinute() {
        return this.datetime.getMinuteOfHour();
    }

    /**
     * Gets the value of {@link #hasDate}, which is set to true once date is
     * set by {@link #setYear(int)}, {@link #setMonth(int)},
     * {@link #setDay(int)} or during construction by
     * {@link #Datetime(Datetime)} or {@link #Datetime(String)}.
     * @return value of {@link #hasDate}
     */
    public boolean getHasDate() {
        return this.hasDate;
    }

    /**
     * Gets the value of {@link #hasTime}, which is set to true once date is
     * set by {@link #setHour(int)}, {@link #setMinute(int)}, or during
     * construction by {@link #Datetime(Datetime)} or
     * {@link #Datetime(String)}.
     * @return value of {@link #hasDate}
     */
    public boolean getHasTime() {
        return this.hasTime;
    }

    /**
     * Gets the datetime's calendar time in milliseconds.
     * ALERT: DO NOT USE THIS TO COMPARE ONLY DATES OR ONLY TIME.
     * @return datetime calendar time in milliseconds
     */
    public long getMillis() {
        if (this.getHasDate() && this.getHasTime()) {
            return this.datetime.getMillis();
        } else if (this.getHasDate()) {
            org.joda.time.DateTime dt = new org.joda.time.DateTime(
                    this.getYear(), this.getMonth(), this.getDay(), 0, 0);

            return dt.getMillis();
        } else if (this.getHasTime()) {
            return (this.getHour()*60 + this.getMinute())*60000;
        } else {
            return 0;
        }
    }

    public Period getDifference(Datetime datetime) {
        return new Period(abs(this.datetime.getMillis() - datetime.getMillis()));
    }

    /**
     * {@link #getYear()}
     * @param year datetime's year
     */
    public void setYear(int year) {
        hasDate = true;
        this.datetime = this.datetime.withYear(year);
    }
    /**
     * {@link #getMonth()}
     * @param month datetime's month
     */
    public void setMonth(int month) {
        hasDate = true;
        this.datetime = this.datetime.withMonthOfYear(month);
    }
    /**
     * {@link #getDay()}
     * @param day datetime's day
     */
    public void setDay(int day) {
        hasDate = true;
        this.datetime = this.datetime.withDayOfMonth(day);
    }

    /**
     * {@link #getHour()}
     * @param hour datetime's hour
     */
    public void setHour(int hour) {
        this.hasTime = true;
        this.datetime = this.datetime.withHourOfDay(hour);
    }

    /**
     * {@link #getMinute()}
     * @param minute datetime's minute
     */
    public void setMinute(int minute) {
        this.hasTime = true;
        this.datetime = this.datetime.withMinuteOfHour(minute);
    }

    /**
     * {@link #getMillis()}
     * @param millis datetime's time in milliseconds
     */
    public void setMillis(long millis) {
        this.hasDate = true;
        this.hasTime = true;
        this.datetime = new org.joda.time.DateTime(millis);
    }

    /**
     * {@link #getHasDate()}
     * @param hasDate whether datetime has date enabled or not
     */
    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    /**
     * {@link #getHasTime()}
     * @param hasTime whether datetime has time enabled or not
     */
    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    /**
     * Returns the a new datetime instance with the period added.
     *
     * @see Period
     * @param period the period to add
     * @return new datetime instance with period added
     */
    public Datetime add(Period period) {
        return new Datetime(this.datetime.withPeriodAdded(period, 1));
    }

    /**
     * Returns the a new datetime instance with the period subtracted.
     *
     * @see Period
     * @param period the period to subtract
     * @return new datetime instance with period subtracted
     */
    public Datetime subtract(Period period) {
        return new Datetime(this.datetime.withPeriodAdded(period, -1));
    }

    /**
     * Returns the datetime's calendar year, month, day, hour and minute
     * values in the format "Y/M/D H:M" where the values do not have a fixed
     * width (they are not padded with zeroes).
     *
     * @return datetime's string equivalent
     */
    @Override
    public String toString() {
        String string = "";
        // Date
        if (this.getHasDate()) {
            string += String.format(Locale.getDefault(), "%1$04d/%2$02d/%3$02d",
                    this.getYear(), this.getMonth(), this.getDay());
        } else {
            string += "0000/00/00";
        }
        // Add a space separator
        string += " ";
        // Time
        if (this.getHasTime()) {
            string += String.format(Locale.getDefault(), "%1$02d:%2$02d",
                    this.getHour(), this.getMinute());
        } else {
            string += "--:--";
        }
        return string;
    }

    /**
     * Returns the datetime's calendar year, month, day, hour and minute
     * depending on whether they exist. The values are formatted in the format
     * "DD/MM/YY HH:MM" when all values are present, "DD/MM/YY" when only the
     * date is present and "HH:MM" when only the time is present.
     *
     * @return datetime formatted string
     */
    public String toFormattedString() {
        String formattedString = "";
        // Add date if it is present
        if (this.getHasDate()) {
            this.datetime.toCalendar(Locale.getDefault());
            formattedString += String.format(Locale.getDefault(),
                    "%1$td/%1$tm/%1$ty", this.datetime.toCalendar(Locale.getDefault()));
        }

        // Add a spacing in between if both exist
        if (this.getHasDate() && this.getHasTime()) {
            formattedString += " ";
        }

        // Add time if it is present
        if (this.getHasTime()) {
            formattedString += String.format(Locale.getDefault(),
                    "%1$tH:%1$tM", this.datetime.toCalendar(Locale.getDefault()));
        }

        return formattedString;
    }

    // Empty describe contents function for parcel
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Function to write the data into a parcelable object.
     *
     * @param out parcel to write to
     * @param flags other flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        // Write hasDate and hasTime
        boolean[] hasDateHasTime = {this.hasDate, this.hasTime};
        out.writeBooleanArray(hasDateHasTime);
        // Write datetime
        out.writeString(default_format.print(this.datetime));

    }

    // Creator constant for parcel
    public static final Parcelable.Creator<Datetime> CREATOR = new Parcelable.Creator<Datetime>() {
        public Datetime createFromParcel(Parcel in) {
            return new Datetime(in);
        }

        public Datetime[] newArray(int size) {
            return new Datetime[size];
        }
    };

    /**
     * Constructor to create instance from data from parcel.
     *
     * @param in parcel to read from
     */
    private Datetime(Parcel in) {
        // Read hasDate and hasTime
        boolean[] hasDateHasTime = in.createBooleanArray();
        this.hasDate = hasDateHasTime[0];
        this.hasTime = hasDateHasTime[1];
        // Parse datetime
        this.datetime = default_format.parseDateTime(in.readString());
    }
}

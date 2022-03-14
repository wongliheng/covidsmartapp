package com.covidsmartapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;

public class SpinnerCreation {
    Context context;
    String [] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda",
            "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
            "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia", "Botswana",
            "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia",
            "Cameroon", "Canada", "Caribbean Netherlands", "Cayman Islands", "Central African Republic", "Chad",
            "Channel Islands", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba",
            "Curaçao", "Cyprus", "Czechia", "Côte d'Ivoire", "DRC", "Denmark", "Diamond Princess", "Djibouti", "Dominica",
            "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
            "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia",
            "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe",
            "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Holy See (Vatican City State)", "Honduras", "Hong Kong",
            "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Jamaica",
            "Japan", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia",
            "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "MS Zaandam",
            "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique",
            "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
            "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nepal", "Netherlands", "New Caledonia", "New Zealand",
            "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea",
            "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Réunion",
            "S. Korea", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre Miquelon",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal",
            "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands",
            "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "St. Barth", "Sudan", "Suriname", "Swaziland",
            "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste",
            "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turks and Caicos Islands", "UAE", "UK", "USA",
            "Uganda", "Ukraine", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Wallis and Futuna",
            "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};

    String [] appointmentType = {"COVID-19 Test", "COVID-19 Vaccination", "Select a type"};
    String [] locations = {"Ang Mo Kio Polyclinic", "Bedok Polyclinic", "Bukit Batok Polyclinic",  "Bukit Merah Polyclinic",
            "Clementi Polyclinic", "Geylang Polyclinic", "Hougang Polyclinic", "Jurong Polyclinic", "Marine Parade Polyclinic",
            "Outram Polyclinic", "Queenstown Polyclinic", "Sengkang Polyclinic", "Tampines Polyclinic", "Toa Payoh Polyclinic",
            "Woodlands Polyclinic", "Yishun Polyclinic", "Select a location"};

    public SpinnerCreation(Context context) {
        this.context = context;
    }

    public SearchableSpinner createCountrySpinner (SearchableSpinner spinner) {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this.context, R.layout.spinner, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryAdapter);

        spinner.setTitle("Select Country");
        spinner.setPositiveButton("OK");

        return spinner;
    }

    public Spinner createAppointmentSpinner (Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, R.layout.spinner, appointmentType) {

            @Override
            public boolean isEnabled(int position) {
                if (position == getCount()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        return spinner;
    }

    public Spinner setUpTimeSpinner (Spinner spinner) {

        String [] oneItemArray = {"Select a date"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, R.layout.spinner, oneItemArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setEnabled(false);
        spinner.setClickable(false);

        return spinner;
    }

    public Spinner createTimeSpinner (Spinner spinner, int day) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        ArrayList<String> timeslots = new ArrayList<String>();

        for (int i = 9; i <= 17; i++)
        {
            String slot1;
            String slot2;
            if (day == currentDay) {
                int hourFromNow = hour + 1;
                if (i > hourFromNow) {
                    if (i > 12) {
                        int hourAM = i - 12;

                        slot1 = hourAM + ":00 PM";
                        slot2 = hourAM + ":30 PM";

                        timeslots.add(slot1);
                        timeslots.add(slot2);
                    } else {
                        slot1 = i + ":00 AM";
                        slot2 = i + ":30 AM";

                        timeslots.add(slot1);
                        timeslots.add(slot2);
                    }
                }
            }
            else {
                if (i > 12) {
                    int hourAM = i - 12;

                    slot1 = hourAM + ":00 PM";
                    slot2 = hourAM + ":30 PM";

                    timeslots.add(slot1);
                    timeslots.add(slot2);
                } else {
                    slot1 = i + ":00 AM";
                    slot2 = i + ":30 AM";

                    timeslots.add(slot1);
                    timeslots.add(slot2);
                }
            }

        }
        if(timeslots.isEmpty()) {
            timeslots.add("No timeslots available");
        }

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this.context, R.layout.spinner, timeslots);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(timeAdapter);
        spinner.setEnabled(true);
        spinner.setClickable(true);

        return spinner;
    }

    public Spinner createLocationSpinner (Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, R.layout.spinner, locations) {

            @Override
            public boolean isEnabled(int position) {
                if (position == getCount()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        return spinner;
    }


}

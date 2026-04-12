---
layout: page
title: User Guide
---

RecruiterPlus is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, RecruiterPlus can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103-F09-2/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your RecruiterPlus.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar recruiterplus.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will display the command reference in the command output.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add -name John Doe -phone 98765432 -email johnd@example.com -address John street, block 123, #01-01` : Adds a contact named `John Doe` to the RecruiterPlus.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add -name NAME`, `NAME` is a parameter which can be used as `add -name John Doe`.

* Items in square brackets are optional.<br>
  e.g. `-name NAME [-tag TAG]` can be used as `-name John Doe -tag friend` or as `-name John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[-tag TAG]…​` can be used as ` ` (i.e. 0 times), `-tag friend`, `-tag friend -tag family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `-name NAME -phone PHONE_NUMBER`, `-phone PHONE_NUMBER -name NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Prints a help message with a description of all commands, as well as a link to this User Guide.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the recruiterplus.

Format: `add -name NAME -phone PHONE_NUMBER -email EMAIL -address ADDRESS -tag TAG…​`
* At least the `-name`, `-phone`, `-email` and `-address` fields must be provided.
* A candidate is considered a duplicate if either `-phone` or `-email` already exists.
* Candidates can share the same name, but both `-phone` and `-email` must each be different from existing candidates.
* `PHONE_NUMBER` must be exactly 8 digits and start with `8` or `9`.
* The `-tag` field is optional.
* The `-tag` field can be used multiple times to add multiple tags to a person. Eg: `-tag friend -tag colleague` adds the tags `friend` and `colleague` to the person.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add -name John Doe -phone 98765432 -email johnd@example.com -address John street, block 123, #01-01`
* `add -name Betsy Crowe -tag friend -email betsycrowe@example.com -address Newgate Prison -phone 92345678 -tag criminal`
* `add -name Bo Yang -phone 87654321 -email boyang@example.com -address Bo's street, block 321, #01-02 -tag colleague -tag friend` to add multiple tags, use -tag [TAG] multiple times.

### Listing all persons : `list`

Shows a list of all persons in the recruiterplus.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the recruiterplus.

Format: `edit INDEX [-name NAME] [-phone PHONE] [-email EMAIL] [-address ADDRESS] [-tag TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
<<<<<<< HEAD
* If `-phone` is provided, it must be exactly 8 digits and start with `8` or `9`.
=======
* Remarks cannot be edited using `edit`.
* To update a remark, use `remark INDEX [REMARK]` and re-enter the full updated remark.
>>>>>>> upstream/master

#### Updating of tags
* Using the `-tag` option will **replace all existing tags** with the newly specified tags.
* Tag updates are **not incremental**, existing tags are not preserved automatically.
* To **retain existing tags**, you must **re-enter** them together with any new tags.
* To **remove all tags**, use `-tag` without specifying any values.
* To add multiple tags, use `-tag [TAG]` multiple times.

Examples:
*  `edit 1 -phone 91234567 -email johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 -name Betsy Crower -tag` Edits the name of the 2nd person to be `Betsy Crower` and **removes all existing
   tags.**
*  `edit 3 -tag friend -tag colleague` Edits the tags of the 3rd person to be `friend` and `colleague`.
* 
#### Note:
>> The `-tag` field does not add to existing tags.<br> Always re-enter tags you wish to keep, or they will be lost.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g. `hans` will match `Hans`
* The search supports typos via fuzzy matching. e.g. `Alicd` will match `Alice`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Partial word matching is supported e.g. `Han` will match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)
* `find aled` returns `Alex Yeoh`<br> ![result for 'find aled'](images/findAledResult.png)

### Filtering persons by interview status: `filter`

Finds persons by interviewed status.

Format: `filter -interviewed INTERVIEWED_STATUS`

* The filter will be done on all persons, not only the currently listed ones.
* Accepted values for `INTERVIEWED_STATUS` are `y/n/1/0`.
* Name-based filtering is not supported by `filter`. Use `find` for name filtering.

Examples:
* `filter -interviewed y` returns persons who are marked as interviewed.
* `filter -interviewed 0` returns persons who are <u>not</u> marked as interviewed.
  ![result for 'filter -interviewed 1'](images/FilterCommandExample.png)

### Deleting a person : `delete`

Deletes the specified person(s) from the recruiterplus.

Format: `delete INDEX [MORE_INDEXES]... | all`

* Deletes the person(s) at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* Multiple indexes can be specified to delete several candidates at once.
* Duplicate indexes are not allowed.
* Use `all` to delete all currently displayed candidates.
* `delete INDEX` or `delete all` on an empty list will show an error message.

Examples:
* `delete 2` deletes the 2nd person in the recruiterplus.
* `delete 1 3 5` deletes the 1st, 3rd and 5th persons in the displayed list.
* `find Betsy` followed by `delete all` deletes all persons in the results of the `find` command.

### Marking a candidate as interviewed : `mark`

Marks the specified candidate as interviewed.

Format: `mark INDEX`

* Marks the candidate at the specified `INDEX` as interviewed.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* A candidate that has already been marked as interviewed cannot be marked again.

Examples:
* `mark 1` marks the 1st candidate in the list as interviewed.
* `find John` followed by `mark 1` marks the 1st candidate in the results of the `find` command as interviewed.

### Unmarking a candidate as not interviewed : `unmark`

Unmarks the specified candidate as interviewed.

Format: `unmark INDEX`

* Unmarks the candidate at the specified `INDEX` as not interviewed.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* A candidate that is already marked as not interviewed cannot be unmarked again.

Examples:
* `unmark 1` unmarks the 1st candidate in the list as not interviewed.
* `find John` followed by `unmark 1` unmarks the 1st candidate in the results of the `find` command as not interviewed.

### Adding a remark to a candidate : `remark`

Adds a new remark or replaces an existing remark for the specified candidate.

Format: `remark INDEX [REMARK]`

* Adds a remark for the candidate at the specified `INDEX`, or replaces the existing remark.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* To update a remark, re-enter the full updated remark. Partial edits are not supported.
* An existing remark will be overwritten by the new remark.
* Using `remark INDEX` without specifying any remark text removes the existing remark.
* A valid remark can consist of zero or more alphanumeric characters (letters and digits), spaces, and the following symbols: `. , ! ? ' " ( ) - / : @ # $ % & + * = [ ]`

Examples:
* `remark 1 Strong in algorithms.` adds the remark "Strong in algorithms." to the 1st candidate.
* `remark 1` removes the remark of the 1st candidate because no remark text is provided.

### Clearing all entries : `clear`

Clears all entries from the recruiterplus.

Format: `clear`

### Exiting the program : `exit`

Exits the program and closes the application.

Format: `exit`

Alias: `bye`

### Saving the data

RecruiterPlus data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

RecruiterPlus data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
Malformed entries are quarantined into `addressbook_invalid.json` during startup preprocessing, while valid entries are kept in the main data file. This helps prevent silent data loss when only some records are invalid.<br>
However, if the overall file is severely corrupted or contains values that violate model constraints, the app may still fail to load some entries. It is recommended to keep a backup before editing the data file manually.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous RecruiterPlus home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then open it again via the `Help` menu, or the keyboard shortcut `F1`, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add -name NAME -phone PHONE_NUMBER -email EMAIL -address ADDRESS [-tag TAG]…​` <br> e.g., `add -name James Ho -phone 92224444 -email jamesho@example.com -address 123, Clementi Rd, 1234665 -tag friend -tag colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [-name NAME] [-phone PHONE_NUMBER] [-email EMAIL] [-address ADDRESS] [-tag TAG]…​`<br> e.g.,`edit 2 -name James Lee -email jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Filter** | `filter -interviewed INTERVIEWED_STATUS`<br> e.g., `filter -interviewed y`
**Mark** | `mark INDEX`<br> e.g., `mark 1`
**Unmark** | `unmark INDEX`<br> e.g., `unmark 1`
**Remark** | `remark INDEX [REMARK]`<br> e.g., `remark 1 Strong in algorithms.`
**List** | `list`
**Help** | `help`

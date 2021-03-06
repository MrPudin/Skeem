package sstinc.skeem.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import sstinc.skeem.R;
import sstinc.skeem.fragments.TaskFragment;
import sstinc.skeem.models.Task;

/**
 * This class dictates what information of the task goes to which view and
 * stores them ina {@link ViewHolder}.
 *
 * @see ArrayAdapter
 * @see ViewHolder
 */
public class TaskArrayAdapter extends ArrayAdapter<Task> {
    // List to contain all the tasks
    public final List<Task> list;

    /**
     * Basic constructor. Uses the list of tasks and the activity context to
     * form the views needed.
     *
     * @param context the activity context
     * @param list the list of tasks
     */
    public TaskArrayAdapter(Activity context, List<Task> list) {
        // Initialize super
        super(context, R.layout.list_task_row, list);

        this.list = list;
    }

    /**
     * This class is used just to hold the values of the views.
     */
    private static class ViewHolder {
        TextView title;
        TextView description;
        CheckBox checkbox;
    }

    /**
     * This method is called when this list adapter is set and the activity
     * needs to display the appropriate view. The correct view for the task
     * should be assembled and returned.
     *
     * @param position position of item in array adapter
     * @param convertView view to inflate in custom view layout
     * @param parent parent viewGroup holding convertViews
     * @return single task's view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Get the task at the position
        Task task = getItem(position);

        if (convertView == null) {
            // Convert View has not been created or has been destroyed.
            // Create a new convert view.
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_task_row, parent, false);

            // Since convert view has not been created or has been destroyed,
            // View holder would also have been lost. Create a new view
            // holder.
            viewHolder = new ViewHolder();

            // Set the view holder's properties to the appropriate views
            viewHolder.title = (TextView) convertView.findViewById(
                    R.id.list_item_task_title);
            viewHolder.description = (TextView) convertView.findViewById(
                    R.id.list_item_task_description);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(
                    R.id.list_item_task_checkBox);

            // Tag the view holder to the convert view so that it can be
            // reused in future calls.
            convertView.setTag(viewHolder);
        } else {
            // The convert view and view holder has been preserved
            // Get view holder from the convert view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Store the position of the checkbox
        viewHolder.checkbox.setTag(position);

        // Set the properties of each view
        viewHolder.title.setText(task.getName());
        viewHolder.description.setText(task.getDescription());
        viewHolder.checkbox.setChecked(list.get(position).checked);

        viewHolder.checkbox.setVisibility(TaskFragment.menu_delete ? View.VISIBLE : View.GONE);

        return convertView;
    }
}

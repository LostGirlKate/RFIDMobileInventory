package lost.girl.rfidmobileinventory.utils

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import lost.girl.rfidmobileinventory.R
import lost.girl.rfidmobileinventory.databinding.CustomDialogBinding
import lost.girl.rfidmobileinventory.databinding.CustomFilterDialogBinding
import lost.girl.rfidmobileinventory.databinding.LayoutLoadingDialogBinding


class UIHelper {
    companion object {
        fun showToastMessage(context: Context, msg: String, textColor: Int) {
            val spannableString = SpannableString(msg)
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        textColor
                    )
                ), 0, spannableString.length, 0
            )
            spannableString.setSpan(
                AbsoluteSizeSpan(50), 0, spannableString.length, 0
            )
            val toast = Toast.makeText(context, spannableString, Toast.LENGTH_SHORT)
            Log.d("InvMobRFID", "toast.show")
            toast.show()
        }


        fun alertProcessDialog(context: Context, msg: String): AlertDialog {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val binding = LayoutLoadingDialogBinding.inflate(LayoutInflater.from(context))
            binding.msgTextView.text = msg
            builder.setCancelable(false)
            builder.setView(binding.root)
            return builder.create()
        }


        fun alertDialog(context: Context, msg: String, onOkClickListener: () -> Unit) {
            val binding = CustomDialogBinding.inflate(LayoutInflater.from(context))
            binding.messageText.text = msg
            val dialogBuilder = MaterialAlertDialogBuilder(
                context,
                R.style.MaterialAlertDialog_rounded
            )
                .setView(binding.root)
                .setCancelable(false)
            val dialog: androidx.appcompat.app.AlertDialog? = dialogBuilder.show()

            binding.btnOk.setOnClickListener {
                dialog?.dismiss()
                onOkClickListener()
            }

            binding.btnCancel.setOnClickListener {
                dialog?.dismiss()
            }

            val percent = 90.0 / 100
            val dm = Resources.getSystem().displayMetrics
            val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
            val percentWidth = rect.width() * percent

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog!!.window?.attributes)
            layoutParams.width = percentWidth.toInt()
            layoutParams.dimAmount = 0.5f
            dialog.window?.attributes = layoutParams
        }

        fun detailDialog(
            context: Context,
            message: String,
            boxColor: Int,
            showBox: Boolean,
            textColor: Int,
            cancelable: Boolean = false
        ): androidx.appcompat.app.AlertDialog {
            val binding = CustomFilterDialogBinding.inflate(LayoutInflater.from(context))
            binding.messageText.text = message
            binding.messageText.setTextColor(context.getColor(textColor))

            if (showBox)
                binding.cardView.findViewById<ConstraintLayout>(R.id.card_view_color).background =
                    AppCompatResources.getDrawable(context, boxColor)
            else
                binding.cardView.visibility = View.GONE

            val dialogBuilder = MaterialAlertDialogBuilder(
                context,
                R.style.MaterialAlertDialog_rounded
            )
                .setView(binding.root)
                .setCancelable(cancelable)
            val dialog: androidx.appcompat.app.AlertDialog? = dialogBuilder.show()
            binding.btnOk.setOnClickListener {
                dialog?.dismiss()
            }
            val percent = 90.0 / 100
            val dm = Resources.getSystem().displayMetrics
            val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
            val percentWidth = rect.width() * percent

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog!!.window?.attributes)
            layoutParams.width = percentWidth.toInt()
            layoutParams.dimAmount = 0.5f
            dialog.window?.attributes = layoutParams

            return dialog
        }


        fun setOnCheckedChangeListenerToFilterButton(
            context: Context,
            button: ToggleButton,
            checkedTextColor: Int,
            message: String,
            boxColor: Int,
            action: () -> Unit
        ) {
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    button.setTextColor(context.getColor(R.color.white))
                    action()
                } else {
                    button.setTextColor(context.getColor(checkedTextColor))
                    action()
                }
            }

            button.setOnLongClickListener {
                detailDialog(context, message, boxColor, true, R.color.white)
                true
            }
        }

        fun refreshToggleButton(button: ToggleButton, text: String) {
            button.textOff = text
            button.textOn = text
            button.isChecked = button.isChecked
        }

    }
}
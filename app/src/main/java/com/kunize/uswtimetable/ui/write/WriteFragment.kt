package com.kunize.uswtimetable.ui.write

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kunize.uswtimetable.NavGraphDirections
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.data.remote.LectureEvaluationEditDto
import com.kunize.uswtimetable.data.remote.LectureEvaluationPostDto
import com.kunize.uswtimetable.data.remote.LectureExamDto
import com.kunize.uswtimetable.databinding.FragmentWriteBinding
import com.kunize.uswtimetable.dataclass.MyEvaluationDto
import com.kunize.uswtimetable.ui.common.EventObserver
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.util.WriteFragmentTitle
import com.kunize.uswtimetable.util.extensions.seekbarChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class WriteFragment : Fragment() {

    lateinit var binding: FragmentWriteBinding

    private val writeViewModel: WriteViewModel by viewModels { ViewModelFactory() }
    private var semesterDialog: SemesterDialog? = null
    private var testDialog: TestDialog? = null
    private val args: WriteFragmentArgs by navArgs()
    private var lectureName = ""
    private var professorName = ""

    private lateinit var gradeRadioBtnList: List<RadioButton>

    private lateinit var testContentCheckBoxList: List<CheckBox>
    private lateinit var difficultyRadioBtnList: List<RadioButton>

    private var defaultHoneyProgress = 6
    private var defaultLearningProgress = 6
    private var defaultSatisfactionProgress = 6

    var lectureId = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write, container, false)

        binding.viewModel = writeViewModel
        binding.lifecycleOwner = this

        lectureId = args.lectureId

        testContentCheckBoxList = listOf(
            binding.genealogyCheckBox,
            binding.textbookCheckBox,
            binding.pptCheckBox,
            binding.noteCheckBox,
            binding.applicationCheckBox,
            binding.practiceCheckBox,
            binding.taskCheckBox
        )

        difficultyRadioBtnList = listOf(
            binding.easyRadioButton,
            binding.normalRadioButton,
            binding.difficultRadioButton
        )

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        setInitValueWhenWrite(args)
        setFragmentViewType(args)
        setInitValueWhenEditMyEvaluation(args)
        setInitValueWhenEditMyExam(args)
        setSeekBarListener()
        setSeekBarProgress()

        binding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.finishButton.setOnClickListener {
            CoroutineScope(IO).launch {

                val emptyMsg = when (binding.writeType.text.toString()) {
                    WriteFragmentTitle.WRITE_EVALUATION, WriteFragmentTitle.EDIT_MY_EVALUATION -> {
                        when {
                            binding.teamRadioGroup.checkedRadioButtonId == -1 -> "???????????? ??????????????????!"
                            binding.taskRadioGroup.checkedRadioButtonId == -1 -> "????????? ??????????????????!"
                            binding.gradeRadioGroup.checkedRadioButtonId == -1 -> "??????????????? ??????????????????!"
                            binding.writeContent.text.toString().isBlank() -> "????????? ??????????????????!"
                            binding.writeContent.text.toString().length < 31 -> "30??? ?????? ???????????????!"
                            else -> ""
                        }
                    }
                    else -> when {
                        getTestContentString().isBlank() -> "?????? ????????? ??????????????????!"
                        binding.difficultyGroup.checkedRadioButtonId == -1 -> "???????????? ??????????????????!"
                        binding.writeContent.text.toString().isBlank() -> "????????? ??????????????????!"
                        binding.writeContent.text.toString().length < 31 -> "30??? ?????? ???????????????!"
                        else -> ""
                    }
                }

                if (emptyMsg.isNotBlank()) {
                    withContext(Main) {
                        Toast.makeText(requireContext(), emptyMsg, Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val response = when (binding.writeType.text.toString()) {
                    WriteFragmentTitle.WRITE_EVALUATION -> writeViewModel.postLectureEvaluation(
                        lectureId,
                        getLectureEvaluationInfo()
                    )
                    WriteFragmentTitle.WRITE_EXAM -> writeViewModel.postLectureExam(
                        lectureId,
                        getLectureExamInfo()
                    )
                    WriteFragmentTitle.EDIT_MY_EVALUATION -> writeViewModel.updateLectureEvaluation(
                        lectureId,
                        getLectureEvaluationEditInfo()
                    )
                    else -> writeViewModel.updateLectureExam(lectureId, getLectureExamEditInfo())
                }

                withContext(Main) {
                    if (response.isSuccessful) {
                        if (args.myExamInfo == null && args.myEvaluation == null)
                            goToLectureInfoFragment()
                        else
                            goToMyPostFragment()
                    } else {
                        writeViewModel.handleError(response.code())
                    }
                }
            }
        }

        writeViewModel.toastViewModel.toastLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                Toast.makeText(
                    requireContext(),
                    writeViewModel.toastViewModel.toastMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        return binding.root
    }

    private fun getLectureEvaluationInfo(): LectureEvaluationPostDto {
        val info: LectureEvaluationPostDto
        with(binding) {
            val team = if (teamNotExistRadioButton.isChecked) 0 else 1
            val difficulty =
                if (gradeGoodRadioButton.isChecked) 0 else if (binding.gradeNormalRadioButton.isChecked) 1 else 2
            val homework =
                if (taskNotExistRadioButton.isChecked) 0 else if (taskNormalRadioButton.isChecked) 1 else 2

            info = LectureEvaluationPostDto(
                lectureName,
                professorName,
                writeViewModel.semesterText.value!!,
                writeViewModel.satisfactionScore.value!!.toFloat(),
                writeViewModel.learningScore.value!!.toFloat(),
                writeViewModel.honeyScore.value!!.toFloat(),
                team,
                difficulty,
                homework,
                writeContent.text.toString()
            )
        }
        return info
    }

    private fun getLectureExamInfo(): LectureExamDto {
        val info: LectureExamDto
        with(binding) {
            var testContent = getTestContentString()
            testContent = testContent.dropLast(2)

            var testDifficulty = ""
            difficultyRadioBtnList.forEach {
                if (it.isChecked)
                    testDifficulty = it.text.toString()
            }

            // TODO ??????
            info = LectureExamDto(
                lectureName = lectureName,
                professor = professorName,
                selectedSemester = writeViewModel.semesterText.value!!,
                examInfo = testContent,
                examType = writeViewModel.testText.value!!,
                examDifficulty = testDifficulty,
                content = writeContent.text.toString()
            )
        }
        return info
    }

    private fun getLectureExamEditInfo(): LectureExamDto {
        val info: LectureExamDto
        with(binding) {
            var testContent = getTestContentString()
            testContent = testContent.dropLast(2)

            var testDifficulty = ""
            difficultyRadioBtnList.forEach {
                if (it.isChecked)
                    testDifficulty = it.text.toString()
            }

            info = LectureExamDto(
                selectedSemester = writeViewModel.semesterText.value!!,
                examInfo = testContent,
                examType = writeViewModel.testText.value!!,
                examDifficulty = testDifficulty,
                content = writeContent.text.toString()
            )
        }
        return info
    }

    private fun getTestContentString(): String {
        var testContent = ""
        testContentCheckBoxList.forEach { checkBox ->
            if (checkBox.isChecked)
                testContent += checkBox.text.toString() + ", "
        }
        return testContent
    }

    private fun getLectureEvaluationEditInfo(): LectureEvaluationEditDto {
        val info: LectureEvaluationEditDto
        with(binding) {
            val team = if (teamNotExistRadioButton.isChecked) 0 else 1
            val difficulty =
                if (gradeGoodRadioButton.isChecked) 0 else if (binding.gradeNormalRadioButton.isChecked) 1 else 2
            val homework =
                if (taskNotExistRadioButton.isChecked) 0 else if (taskNormalRadioButton.isChecked) 1 else 2

            info = LectureEvaluationEditDto(
                writeViewModel.semesterText.value!!,
                writeViewModel.satisfactionScore.value!!.toFloat(),
                writeViewModel.learningScore.value!!.toFloat(),
                writeViewModel.honeyScore.value!!.toFloat(),
                team,
                difficulty,
                homework,
                writeContent.text.toString()
            )
        }
        return info
    }

    private fun setSeekBarListener() {
        binding.honeySeekBar.seekbarChangeListener {
            writeViewModel.changeHoneyScore(it)
        }

        binding.satisfactionSeekBar.seekbarChangeListener {
            writeViewModel.changeSatisfactionScore(it)
        }

        binding.learningSeekBar.seekbarChangeListener {
            writeViewModel.changeLearningScore(it)
        }
    }

    private fun setInitValueWhenEditMyExam(args: WriteFragmentArgs) {
        args.myExamInfo?.let {
            setLectureProfessorName(it)
            setTestContentCheckBox(it)
            setDifficultyRadioBtn(it)
            val list = it.semesterList?.replace(" ", "")?.split(",") ?: return@let
            setSemesterSpinnerList(list)
            // TODO MyExamInfo ???????????? ????????? ?????? ?????? ?????? ("????????????" ?????? ??? -> spinner ?????? ???????????? ???????????? ?????????)
            writeViewModel.initSemesterText(it.selectedSemester ?: return@let)
            binding.writeContent.setText(it.content)
            binding.writeType.text = WriteFragmentTitle.EDIT_MY_EXAM
            binding.finishButton.text = WriteFragmentTitle.FINISH_EDIT
        }
    }

    private fun setDifficultyRadioBtn(it: LectureExamDto) {
        for (radioButton in difficultyRadioBtnList) {
            if (it.examDifficulty == radioButton.text.toString()) {
                radioButton.isChecked = true
                break
            }
        }
    }

    private fun setTestContentCheckBox(it: LectureExamDto) {
        for (checkBox in testContentCheckBoxList) {
            for (dataString in it.examInfo.replace(" ", "").split(",")) {
                if (checkBox.text == dataString)
                    checkBox.isChecked = true
            }
        }
    }

    private fun setLectureProfessorName(it: LectureExamDto) {
        lectureName = it.lectureName ?: return
        professorName = it.professor ?: return
    }

    private fun setFragmentViewType(args: WriteFragmentArgs) {
        if (args.isEvaluation) {
            binding.tvTestType.visibility = View.GONE
            binding.clSelectTestType.visibility = View.GONE
            binding.testGroup.visibility = View.GONE
            binding.writeType.text = WriteFragmentTitle.WRITE_EVALUATION
            binding.finishButton.text = WriteFragmentTitle.FINISH_EVALUATION
            binding.writeContent.hint = "??????????????? ??????????????????."
        } else {
            binding.lectureGroup.visibility = View.GONE
            binding.writeType.text = WriteFragmentTitle.WRITE_EXAM
            binding.finishButton.text = WriteFragmentTitle.FINISH_EXAM
            binding.writeContent.hint = "????????? ?????? ??????, ?????????, ???????????? ?????? ???????????? ??????????????????."
        }
    }

    private fun setInitValueWhenWrite(args: WriteFragmentArgs) {
        args.lectureProfessorName?.let {
            lectureName = it.subject
            professorName = it.professor
            setSemesterSpinnerList(args.lectureProfessorName!!.semester.replace(" ", "").split(","))
        }
        setTestSpinnerList(listOf("????????????", "????????????", "??????", "??????"))
    }

    private fun setTestSpinnerList(list: List<String>) {
        writeViewModel.initTestText(list[0])

        binding.clSelectTestType.setOnClickListener {
            testDialog = TestDialog(context as AppCompatActivity, writeViewModel, list)
            testDialog?.show()
        }

        writeViewModel.dialogTestClickEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                testDialog?.dismiss()
            }
        )
    }

    private fun setSemesterSpinnerList(list: List<String>) {
        writeViewModel.initSemesterText(list[0])

        binding.clSelectSemester.setOnClickListener {
            semesterDialog = SemesterDialog(context as AppCompatActivity, writeViewModel, list)
            semesterDialog?.show()
        }

        writeViewModel.dialogSemesterClickEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                semesterDialog?.dismiss()
            }
        )
    }

    private fun setInitValueWhenEditMyEvaluation(args: WriteFragmentArgs) {
        args.myEvaluation?.let {
            setLectureProfessorName(it)
            setDefaultSeekBarProgressValue(it)
            setTeamRadioBtn(it)
            setTaskRadioBtn(it)
            setGradeRadioBtn(it)

            val list = it.semesterList.replace(" ", "").split(",")
            setSemesterSpinnerList(list)
            writeViewModel.initSemesterText(it.selectedSemester)
            binding.writeContent.setText(it.content)
            binding.writeType.text = WriteFragmentTitle.EDIT_MY_EVALUATION
            binding.finishButton.text = WriteFragmentTitle.FINISH_EDIT
        }
    }

    private fun setGradeRadioBtn(it: MyEvaluationDto) {
        gradeRadioBtnList = listOf(
            binding.gradeGoodRadioButton,
            binding.gradeNormalRadioButton,
            binding.gradeDifficultRadioButton
        )
        gradeRadioBtnList[it.difficulty].isChecked = true
    }

    private fun setTaskRadioBtn(it: MyEvaluationDto) {
        when (it.homework) {
            0 -> binding.taskNotExistRadioButton.isChecked = true
            1 -> binding.taskNormalRadioButton.isChecked = true
            2 -> binding.taskManyRadioButton.isChecked = true
        }
    }

    private fun setTeamRadioBtn(it: MyEvaluationDto) {
        if (it.team == 1)
            binding.teamExistRadioButton.isChecked = true
        else
            binding.teamNotExistRadioButton.isChecked = true
    }

    private fun setDefaultSeekBarProgressValue(it: MyEvaluationDto) {
        defaultHoneyProgress = (it.honey * 2).roundToInt()
        defaultLearningProgress = (it.learning * 2).roundToInt()
        defaultSatisfactionProgress = (it.satisfaction * 2).roundToInt()
    }

    private fun setLectureProfessorName(it: MyEvaluationDto) {
        lectureName = it.lectureName
        professorName = it.professor
    }

    private fun setSeekBarProgress() {
        /*
        * progress ???????????? ?????? ?????? ??????
        * ???????????? ?????? -> ?????? ?????? 2, ?????? ?????? 4??? ?????? -> ??????
        * ??? ?????? ?????? ??? ?????? ???????????? ????????? ????????? ??????
        * ?????? ?????? ??? ?????? ????????? 0?????? ????????? ?????? ?????? 2??? 4??? ????????????
        * AVD ??? V20????????? ?????? ????????? ??????????????? ????????? s10e ????????? ???????????? ??????
        *
        * ?????? ????????? ???????????? ?????? progress ?????????
        * */
        binding.satisfactionSeekBar.progress = defaultHoneyProgress
        binding.learningSeekBar.progress = defaultLearningProgress
        binding.honeySeekBar.progress = defaultSatisfactionProgress
    }

    private fun goToLectureInfoFragment() {
        val action = NavGraphDirections.actionGlobalLectureInfoFragment(lectureId = lectureId)
        findNavController().navigate(action)
    }

    private fun goToMyPostFragment() {
        val action = NavGraphDirections.actionGlobalMyPostFragment()
        findNavController().navigate(action)
    }
}
